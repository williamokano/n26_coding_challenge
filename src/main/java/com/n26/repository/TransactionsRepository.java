package com.n26.repository;

import com.n26.model.Statistics;
import com.n26.model.Transaction;

import java.util.List;

public interface TransactionsRepository {
    List<Statistics> getAllStatistics();

    void save(Transaction transaction);

    void clearTransactions();
}
