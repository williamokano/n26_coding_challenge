package com.n26.controller

import com.n26.api.request.CreateTransactionRequest
import com.n26.exception.TooOldTransactionException
import com.n26.exception.TransactionIsInTheFutureException
import com.n26.model.Transaction
import com.n26.service.TransactionsService
import org.modelmapper.ModelMapper
import spock.lang.Specification

import java.time.LocalDateTime

class TransactionsControllerTest extends Specification {
    TransactionsService transactionsService = Mock(TransactionsService)
    ModelMapper mapper = Mock(ModelMapper)
    TransactionsController controller = new TransactionsController(transactionsService, mapper)

    CreateTransactionRequest request = CreateTransactionRequest.builder()
            .amount(new BigDecimal("102"))
            .timestamp(LocalDateTime.now())
            .build()

    Transaction transaction = Transaction.builder()
            .amount(new BigDecimal("102"))
            .timestamp(LocalDateTime.now())
            .build()

    def "setup"() {
        LocalDateTime now = LocalDateTime.now()
        request.setTimestamp(now)
        transaction.setTimestamp(now)
    }

    def "should create transaction"() {
        when:
        controller.createTransaction(request)

        then:
        1 * mapper.map(request, Transaction.class) >> transaction
        1 * transactionsService.addTransaction(transaction)

        then:
        notThrown(Exception)
    }

    def "should not create transaction in the future"() {
        when:
        request.setTimestamp(LocalDateTime.now().plusSeconds(120L))
        controller.createTransaction(request)

        then:
        1 * mapper.map(request, Transaction.class) >> transaction
        1 * transactionsService.addTransaction(transaction) >> { throw new TooOldTransactionException() }

        then:
        thrown(TooOldTransactionException)
    }

    def "should not create transactions outside the transactions tumbling window"() {
        when:
        request.setTimestamp(LocalDateTime.now().plusSeconds(120L))
        controller.createTransaction(request)

        then:
        1 * mapper.map(request, Transaction.class) >> transaction
        1 * transactionsService.addTransaction(transaction) >> { throw new TransactionIsInTheFutureException() }

        then:
        thrown(TransactionIsInTheFutureException)
    }

    def "should delete all transactions"() {
        when:
        controller.deleteTransactions()

        then:
        1 * transactionsService.clearTransactions()

        then:
        notThrown(Exception)
    }
}
