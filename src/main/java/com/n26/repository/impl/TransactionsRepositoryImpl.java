package com.n26.repository.impl;

import com.n26.config.ConfigResolver;
import com.n26.model.SecondTransactionsPair;
import com.n26.model.Transaction;
import com.n26.repository.TransactionsRepository;
import com.n26.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Repository
public class TransactionsRepositoryImpl implements TransactionsRepository {

    private final ConfigResolver configResolver;
    private ConcurrentHashMap<Long, SecondTransactionsPair> hashMap;

    @Autowired
    public TransactionsRepositoryImpl(ConfigResolver configResolver) {
        this.configResolver = configResolver;
        this.hashMap = new ConcurrentHashMap<>(configResolver.transactionsWindowInSeconds());
    }


    @Override
    public List<Transaction> findAll() {
        return hashMap.values().stream().map(SecondTransactionsPair::getTransactions)
                .flatMap(List::stream)
                .filter(this::isAfterBeginningOfTransactionsWindow)
                .collect(Collectors.toList());

    }

    @Override
    public void save(Transaction transaction) {
        Long transactionsBucket = findTransactionBucket(transaction);
        hashMap.compute(transactionsBucket, computeTransaction(transaction));
    }

    private BiFunction<Long, SecondTransactionsPair, SecondTransactionsPair> computeTransaction(Transaction transaction) {
        long timestampInSeconds = DateUtil.getEpochSecondsFromLocalDateTime(transaction.getTimestamp());
        return (bucket, secondTransactionsPair) -> {
            if (secondTransactionsPair == null || secondTransactionsPair.getSecond() != timestampInSeconds) {
                return createNewSecondTransactionPair(transaction);
            }

            secondTransactionsPair.getTransactions().add(transaction);
            return secondTransactionsPair;
        };
    }

    private SecondTransactionsPair createNewSecondTransactionPair(Transaction transaction) {
        ArrayList<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        return SecondTransactionsPair.builder()
                .second(DateUtil.getEpochSecondsFromLocalDateTime(transaction.getTimestamp()))
                .transactions(transactions)
                .build();
    }

    @Override
    public void clearTransactions() {
        this.hashMap.clear();
    }

    private Long findTransactionBucket(Transaction transaction) {
        long timestampInSeconds = DateUtil.getEpochSecondsFromLocalDateTime(transaction.getTimestamp());
        return timestampInSeconds % configResolver.transactionsWindowInSeconds();
    }

    private boolean isAfterBeginningOfTransactionsWindow(Transaction transaction) {
        LocalDateTime beginningOfTransactionsWindow = LocalDateTime.now().minusSeconds(configResolver.transactionsWindowInSeconds());
        return transaction.getTimestamp().isAfter(beginningOfTransactionsWindow);
    }
}
