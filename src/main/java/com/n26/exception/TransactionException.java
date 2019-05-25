package com.n26.exception;

import com.n26.model.Transaction;
import lombok.Getter;

public class TransactionException extends RuntimeException {
    @Getter
    protected final Transaction transaction;

    TransactionException(Transaction transaction) {
        super();
        this.transaction = transaction;
    }
}
