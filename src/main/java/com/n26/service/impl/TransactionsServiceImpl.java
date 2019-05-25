package com.n26.service.impl;

import com.n26.exception.TransactionIsInTheFutureException;
import com.n26.model.Transaction;
import com.n26.service.TransactionsService;
import com.n26.utils.DateUtil;
import org.springframework.stereotype.Service;

@Service
public class TransactionsServiceImpl implements TransactionsService {
    @Override
    public void addTransaction(Transaction transaction) {
        validateTransaction(transaction);
    }

    @Override
    public void clearTransactions() {

    }

    private void validateTransaction(Transaction transaction) {
        if (DateUtil.getEpochFromLocalDate(transaction.getTimestamp()) > System.currentTimeMillis()) {
            throw new TransactionIsInTheFutureException();
        }
    }
}
