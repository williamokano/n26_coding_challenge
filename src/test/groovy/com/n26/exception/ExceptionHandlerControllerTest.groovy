package com.n26.exception

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.n26.config.ConfigResolver
import com.n26.model.Transaction
import org.springframework.http.converter.HttpMessageNotReadableException
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

    def "should handle http message not readable when failed to parse"() {
        given:
        InvalidFormatException cause = Mock(InvalidFormatException)
        HttpMessageNotReadableException mainEx = new HttpMessageNotReadableException("", cause)

        when:
        def responseEntity = exceptionHandlerController.handleHttpMessageNotReadableException(mainEx)

        then:
        responseEntity.statusCodeValue == 422
    }

    def "should handle http message not readable when failed to map to object"() {
        given:
        JsonParseException cause = Mock(JsonParseException)
        HttpMessageNotReadableException mainEx = new HttpMessageNotReadableException("", cause)

        when:
        def responseEntity = exceptionHandlerController.handleHttpMessageNotReadableException(mainEx)

        then:
        responseEntity.statusCodeValue == 400
    }
}
