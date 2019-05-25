package com.n26.service;

import com.n26.model.Transaction;

public interface TransactionsService {
    void addTransaction(Transaction transaction);

    void clearTransactions();
}
