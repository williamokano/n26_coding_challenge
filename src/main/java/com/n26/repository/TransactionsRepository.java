package com.n26.repository;

import com.n26.model.Transaction;

import java.util.List;

public interface TransactionsRepository {
    List<Transaction> findAll();

    void save(Transaction transaction);

    void clearTransactions();
}
