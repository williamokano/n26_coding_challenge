package com.n26.service.impl

import com.n26.exception.TransactionIsInTheFutureException
import com.n26.model.Transaction
import com.n26.service.TransactionsService
import spock.lang.Specification

import java.time.LocalDateTime

class TransactionsServiceImplTest extends Specification {

    TransactionsService transactionService = new TransactionsServiceImpl()

    def "should throw exception if transaction is in the future"() {
        given:
        def transaction = Transaction.builder()
                .amount(new BigDecimal("1000.00"))
                .timestamp(LocalDateTime.now().plusHours(1L))
                .build()

        when:
        transactionService.addTransaction(transaction)

        then:
        thrown(TransactionIsInTheFutureException)
    }
}
