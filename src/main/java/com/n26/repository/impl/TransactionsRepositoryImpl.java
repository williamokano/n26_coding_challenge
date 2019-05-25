package com.n26.repository.impl;

import com.n26.model.Transaction;
import com.n26.repository.TransactionsRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TransactionsRepositoryImpl implements TransactionsRepository {
    @Override
    public List<Transaction> findAll() {
        return null;
    }

    @Override
    public void save(Transaction transaction) {

    }

    @Override
    public void clearTransactions() {

    }
}
