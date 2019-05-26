package com.n26.service.impl;

import com.n26.model.Statistics;
import com.n26.repository.TransactionsRepository;
import com.n26.service.StatisticsService;
import com.n26.utils.BigDecimalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    private final TransactionsRepository transactionsRepository;

    @Autowired
    public StatisticsServiceImpl(TransactionsRepository transactionsRepository) {
        this.transactionsRepository = transactionsRepository;
    }

    @Override
    public Statistics getStatistics() {
        return processTransactions().roundHalfUp();
    }

    private Statistics processTransactions() {
        Statistics responseStatistics = Statistics.empty();
        transactionsRepository.getAllStatistics().forEach(statistics -> aggregateStatistics(responseStatistics, statistics));
        return responseStatistics;
    }

    private void aggregateStatistics(Statistics first, Statistics second) {
        first.setSum(first.getSum().add(second.getSum()));
        first.setMin(first.getCount() == 0 ? second.getMin() : BigDecimalUtil.min(first.getMin(), second.getMin()));
        first.setMax(first.getCount() == 0 ? second.getMax() : BigDecimalUtil.max(first.getMax(), second.getMax()));
        first.setCount(first.getCount() + second.getCount());
        first.setAvg(BigDecimalUtil.avg(first.getSum(), first.getCount()));
    }
}
