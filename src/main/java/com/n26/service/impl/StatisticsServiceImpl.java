package com.n26.service.impl;

import com.n26.model.Statistic;
import com.n26.model.Transaction;
import com.n26.repository.TransactionsRepository;
import com.n26.service.StatisticsService;
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
    public Statistic getStatistics() {
        Statistic statistic = new Statistic();
        transactionsRepository.findAll().forEach(transaction -> {

        });

        return statistic;
    }
}
