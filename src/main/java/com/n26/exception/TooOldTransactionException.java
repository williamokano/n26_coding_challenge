package com.n26.exception;

import com.n26.model.Transaction;

public class TooOldTransactionException extends TransactionException {
    public TooOldTransactionException(Transaction transaction) {
        super(transaction);
    }
}
