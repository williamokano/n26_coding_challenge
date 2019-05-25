package com.n26.exception;

import com.n26.config.ConfigResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {

    private final Logger logger = LoggerFactory.getLogger(ExceptionHandlerController.class);
    private final ConfigResolver configResolver;

    @Autowired
    ExceptionHandlerController(ConfigResolver configResolver) {
        this.configResolver = configResolver;
    }

    @ExceptionHandler(TransactionIsInTheFutureException.class)
    public ResponseEntity<?> handleTransactionsIsInTheFutureException(TransactionIsInTheFutureException ex) {
        logger.warn("Transaction {} was ignored due to future timestamp", ex.getTransaction());
        return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(TooOldTransactionException.class)
    public ResponseEntity<?> handleTooOldTransactionException(TooOldTransactionException ex) {
        logger.warn("Transaction {} was ignored because it's not inside the statistics window of {} seconds", ex.getTransaction(), configResolver.transactionsWindowInSeconds());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
