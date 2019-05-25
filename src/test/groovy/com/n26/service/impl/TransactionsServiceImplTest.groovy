package com.n26.service.impl

import com.n26.config.ConfigResolver
import com.n26.exception.TooOldTransactionException
import com.n26.exception.TransactionIsInTheFutureException
import com.n26.model.Transaction
import com.n26.repository.TransactionsRepository
import spock.lang.Specification

import java.time.LocalDateTime

class TransactionsServiceImplTest extends Specification {
    ConfigResolver configResolver = Mock(ConfigResolver)
    TransactionsRepository transactionsRepository = Mock(TransactionsRepository)

    TransactionsServiceImpl transactionService = new TransactionsServiceImpl(configResolver, transactionsRepository)

    def "should save transactions"() {
        given:
        def transaction = Transaction.builder()
                .amount(new BigDecimal("1000.00"))
                .timestamp(LocalDateTime.now())
                .build()

        when:
        transactionService.addTransaction(transaction)

        then:
        1 * configResolver.transactionsWindowInSeconds() >> 60
        1 * transactionsRepository.save(transaction)
    }

    def "should clear transactions"() {
        when:
        transactionService.clearTransactions()

        then:
        1 * transactionsRepository.clearTransactions()
    }

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

    def "should throw exception if transaction is before the tumbling window"() {
        given:
        def transaction = Transaction.builder()
                .amount(new BigDecimal("1000.00"))
                .timestamp(LocalDateTime.now().minusHours(1L))
                .build()

        when:
        transactionService.addTransaction(transaction)

        then:
        1 * configResolver.transactionsWindowInSeconds() >> 60
        thrown(TooOldTransactionException)
    }
}
