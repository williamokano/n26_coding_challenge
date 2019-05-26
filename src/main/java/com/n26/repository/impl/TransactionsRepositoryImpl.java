package com.n26.repository.impl;

import com.n26.config.ConfigResolver;
import com.n26.model.Statistics;
import com.n26.model.TimeStatisticPair;
import com.n26.model.Transaction;
import com.n26.repository.TransactionsRepository;
import com.n26.utils.BigDecimalUtil;
import com.n26.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Repository
public class TransactionsRepositoryImpl implements TransactionsRepository {

    private final ConfigResolver configResolver;
    private ConcurrentHashMap<Long, TimeStatisticPair> hashMap;

    @Autowired
    public TransactionsRepositoryImpl(ConfigResolver configResolver) {
        this.configResolver = configResolver;
        this.hashMap = new ConcurrentHashMap<>(configResolver.transactionsWindowInSeconds());
    }


    @Override
    public List<Statistics> getAllStatistics() {
        return hashMap.values().stream()
                .filter(this::isAfterBeginningOfTransactionsTimeWindow)
                .map(TimeStatisticPair::getStatistic)
                .collect(Collectors.toList());

    }

    @Override
    public void save(Transaction transaction) {
        Long transactionsBucket = findTransactionBucket(transaction);
        hashMap.compute(transactionsBucket, computeTransaction(transaction));
    }

    @Override
    public void clearTransactions() {
        this.hashMap.clear();
    }

    private BiFunction<Long, TimeStatisticPair, TimeStatisticPair> computeTransaction(Transaction transaction) {
        return (bucket, timeStatisticPair) -> {
            if (bucketHasNoValue(timeStatisticPair)) {
                return createTimeStatisticPairFromTransaction(transaction);
            }

            if (bucketAndTransactionNotInTheSameEpoch(timeStatisticPair, transaction)) {
                return createTimeStatisticPairFromTransaction(transaction);
            }

            updateStatistics(timeStatisticPair, transaction);
            return timeStatisticPair;
        };
    }

    private boolean bucketHasNoValue(TimeStatisticPair timeStatisticPair) {
        return timeStatisticPair == null;
    }

    private boolean bucketAndTransactionNotInTheSameEpoch(TimeStatisticPair timeStatisticPair, Transaction transaction) {
        long bucketSeconds = DateUtil.getEpochSecondsFromLocalDateTime(timeStatisticPair.getTime());
        long transactionSeconds = DateUtil.getEpochSecondsFromLocalDateTime(transaction.getTimestamp());
        return bucketSeconds != transactionSeconds;
    }

    private void updateStatistics(TimeStatisticPair secondStatisticPair, Transaction transaction) {
        Statistics statistic = secondStatisticPair.getStatistic();
        statistic.setSum(statistic.getSum().add(transaction.getAmount()));
        statistic.setMin(BigDecimalUtil.min(statistic.getMin(), transaction.getAmount()));
        statistic.setMax(BigDecimalUtil.max(statistic.getMax(), transaction.getAmount()));
        statistic.setCount(statistic.getCount() + 1);

        statistic.setAvg(BigDecimalUtil.avg(statistic.getSum(), statistic.getCount()));
    }

    private TimeStatisticPair createTimeStatisticPairFromTransaction(Transaction transaction) {
        return TimeStatisticPair.builder()
                .time(transaction.getTimestamp())
                .statistic(Statistics.builder()
                        .sum(transaction.getAmount())
                        .avg(transaction.getAmount())
                        .min(transaction.getAmount())
                        .max(transaction.getAmount())
                        .count(1L)
                        .build())
                .build();
    }

    private Long findTransactionBucket(Transaction transaction) {
        long timestampInSeconds = DateUtil.getEpochSecondsFromLocalDateTime(transaction.getTimestamp());
        return timestampInSeconds % configResolver.transactionsWindowInSeconds();
    }

    private boolean isAfterBeginningOfTransactionsTimeWindow(TimeStatisticPair secondStatisticPair) {
        LocalDateTime beginningOfTransactionsWindow = LocalDateTime.now().minusSeconds(configResolver.transactionsWindowInSeconds());
        return secondStatisticPair.getTime().isAfter(beginningOfTransactionsWindow);
    }
}
