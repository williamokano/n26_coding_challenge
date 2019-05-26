package com.n26.service.impl;

import com.n26.model.Statistics;
import com.n26.repository.TransactionsRepository;
import com.n26.service.StatisticsService;
import com.n26.utils.BigDecimalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    private final TransactionsRepository transactionsRepository;

    @Autowired
    public StatisticsServiceImpl(TransactionsRepository transactionsRepository) {
        this.transactionsRepository = transactionsRepository;
    }

    @Override
    public Statistics getStatistics() {
        Statistics statistic = processTransactions();

        return formatRoundHalfUp(statistic);
    }

    private Statistics formatRoundHalfUp(Statistics statistic) {
        statistic.setSum(statistic.getSum().setScale(2, BigDecimal.ROUND_HALF_UP));
        statistic.setAvg(statistic.getAvg().setScale(2, BigDecimal.ROUND_HALF_UP));
        statistic.setMin(statistic.getMin().setScale(2, BigDecimal.ROUND_HALF_UP));
        statistic.setMax(statistic.getMax().setScale(2, BigDecimal.ROUND_HALF_UP));

        return statistic;
    }

    private Statistics processTransactions() {
        Statistics responseStatistics = Statistics.builder()
                .sum(new BigDecimal(0))
                .avg(new BigDecimal(0))
                .min(new BigDecimal(0))
                .max(new BigDecimal(0))
                .count(0L)
                .build();

        transactionsRepository.getAllStatistics().forEach(statistics -> mergeStatistics(responseStatistics, statistics));

        return responseStatistics;
    }

    private void mergeStatistics(Statistics first, Statistics second) {
        first.setSum(first.getSum().add(second.getSum()));
        first.setMin(first.getCount() == 0 ? second.getMin() : BigDecimalUtil.min(first.getMin(), second.getMin()));
        first.setMax(first.getCount() == 0 ? second.getMax() : BigDecimalUtil.max(first.getMax(), second.getMax()));
        first.setCount(first.getCount() + second.getCount());
        first.setAvg(BigDecimalUtil.avg(first.getSum(), first.getCount()));
    }
}
