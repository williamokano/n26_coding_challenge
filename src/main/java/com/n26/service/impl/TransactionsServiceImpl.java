package com.n26.service.impl;

import com.n26.config.ConfigResolver;
import com.n26.exception.TooOldTransactionException;
import com.n26.exception.TransactionIsInTheFutureException;
import com.n26.model.Transaction;
import com.n26.repository.TransactionsRepository;
import com.n26.service.TransactionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransactionsServiceImpl implements TransactionsService {

    private final ConfigResolver configResolver;
    private final TransactionsRepository transactionsRepository;

    @Autowired
    TransactionsServiceImpl(ConfigResolver configResolver, TransactionsRepository transactionsRepository) {
        this.configResolver = configResolver;
        this.transactionsRepository = transactionsRepository;
    }

    @Override
    public void addTransaction(Transaction transaction) {
        validateTransaction(transaction);
        transactionsRepository.save(transaction);
    }

    @Override
    public void clearTransactions() {
        transactionsRepository.clearTransactions();
    }

    private void validateTransaction(Transaction transaction) {
        validateTransactionIsInsideStatisticsWindow(transaction, LocalDateTime.now());
    }

    private void validateTransactionIsInsideStatisticsWindow(Transaction transaction, LocalDateTime now) {
        validateTransactionIsNotInTheFuture(transaction, now);
        validateTransactionIsNotOutsidePastWindow(transaction, now);
    }

    private void validateTransactionIsNotInTheFuture(Transaction transaction, LocalDateTime now) {
        if (transaction.getTimestamp().isAfter(now)) {
            throw new TransactionIsInTheFutureException(transaction);
        }
    }

    private void validateTransactionIsNotOutsidePastWindow(Transaction transaction, LocalDateTime now) {
        LocalDateTime beginningStatisticsWindow = now.minusSeconds(configResolver.transactionsWindowInSeconds());
        if (transaction.getTimestamp().isBefore(beginningStatisticsWindow)) {
            throw new TooOldTransactionException(transaction);
        }
    }
}
