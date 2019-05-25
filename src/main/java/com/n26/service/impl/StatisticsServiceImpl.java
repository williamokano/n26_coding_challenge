package com.n26.service.impl;

import com.n26.model.Statistic;
import com.n26.model.Transaction;
import com.n26.repository.TransactionsRepository;
import com.n26.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static com.n26.utils.StatisticUtil.updateStatisticMaxIfTransactionIsGreater;
import static com.n26.utils.StatisticUtil.updateStatisticMinIfTransactionIsLesser;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    private final TransactionsRepository transactionsRepository;

    @Autowired
    public StatisticsServiceImpl(TransactionsRepository transactionsRepository) {
        this.transactionsRepository = transactionsRepository;
    }

    @Override
    public Statistic getStatistics() {
        Statistic statistic = processTransactions();

        return formatRoundHalfUp(statistic);
    }

    private Statistic formatRoundHalfUp(Statistic statistic) {
        statistic.setSum(statistic.getSum().setScale(2, BigDecimal.ROUND_HALF_UP));
        statistic.setAvg(statistic.getAvg().setScale(2, BigDecimal.ROUND_HALF_UP));
        statistic.setMin(statistic.getMin().setScale(2, BigDecimal.ROUND_HALF_UP));
        statistic.setMax(statistic.getMax().setScale(2, BigDecimal.ROUND_HALF_UP));

        return statistic;
    }

    private Statistic processTransactions() {
        List<Transaction> transactions = transactionsRepository.findAll();
        Statistic statistic = createStatisticFromTransactionsList(transactions);

        transactions.forEach(transaction -> {
            updateStatisticMinIfTransactionIsLesser(statistic, transaction);
            updateStatisticMaxIfTransactionIsGreater(statistic, transaction);

            updateSum(statistic, transaction);
            increaseCount(statistic);
        });

        if (statistic.getCount() > 0) {
            statistic.setAvg(calculateAverage(statistic));
        }

        return statistic;
    }

    private Statistic createStatisticFromTransactionsList(List<Transaction> transactions) {
        Statistic.StatisticBuilder builder = Statistic.builder()
                .sum(BigDecimal.ZERO)
                .avg(BigDecimal.ZERO)
                .max(BigDecimal.ZERO)
                .min(BigDecimal.ZERO)
                .count(0L);

        if (!transactions.isEmpty()) {
            builder.min(transactions.get(0).getAmount())
                    .max(transactions.get(0).getAmount())
                    .build();
        }

        return builder.build();
    }

    private void updateSum(Statistic statistic, Transaction transaction) {
        statistic.setSum(statistic.getSum().add(transaction.getAmount()));
    }

    private void increaseCount(Statistic statistic) {
        statistic.setCount(statistic.getCount() + 1);
    }

    private BigDecimal calculateAverage(Statistic statistic) {
        return new BigDecimal(statistic.getSum().doubleValue() / statistic.getCount());
    }
}
