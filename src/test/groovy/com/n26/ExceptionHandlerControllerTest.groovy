package com.n26

import com.n26.config.ConfigResolver
import com.n26.exception.ExceptionHandlerController
import com.n26.exception.TooOldTransactionException
import com.n26.exception.TransactionIsInTheFutureException
import com.n26.model.Transaction
import spock.lang.Specification

class ExceptionHandlerControllerTest extends Specification {
    ConfigResolver configResolver = Mock(ConfigResolver)
    ExceptionHandlerController exceptionHandlerController = new ExceptionHandlerController(configResolver)

    def "should handle too old exception"() {
        given:
        TooOldTransactionException transactionException = new TooOldTransactionException(new Transaction())

        when:
        def responseEntity = exceptionHandlerController.handleTooOldTransactionException(transactionException)

        then:
        responseEntity.statusCodeValue == 204
    }

    def "should handle transactions is in the future"() {
        given:
        TransactionIsInTheFutureException transactionException = new TransactionIsInTheFutureException(new Transaction())

        when:
        def responseEntity = exceptionHandlerController.handleTransactionsIsInTheFutureException(transactionException)

        then:
        responseEntity.statusCodeValue == 422
    }
}
