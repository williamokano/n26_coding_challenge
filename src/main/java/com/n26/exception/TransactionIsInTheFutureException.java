package com.n26.exception;

import com.n26.model.Transaction;

public class TransactionIsInTheFutureException extends TransactionException {
    public TransactionIsInTheFutureException(Transaction transaction) {
        super(transaction);
    }
}
