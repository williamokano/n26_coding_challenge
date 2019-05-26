package com.n26.repository.impl

import com.n26.config.ConfigResolver
import com.n26.model.Transaction
import spock.lang.Specification

import static com.n26.utils.TestUtils.buildTransaction

class TransactionsRepositoryImplTest extends Specification {
    ConfigResolver configResolver = Mock(ConfigResolver)
    TransactionsRepositoryImpl repository = new TransactionsRepositoryImpl(configResolver)

    def "should save transaction"() {
        given:
        Transaction transaction = buildTransaction("10")

        when:
        repository.save(transaction)
        def transactions = repository.getAllStatistics()

        then:
        2 * configResolver.transactionsWindowInSeconds() >> 60
        transactions.size() == 1
    }

    def "should clear all transactions"() {
        given:
        List<Transaction> transactions = [buildTransaction("5"), buildTransaction("3"), buildTransaction("3")]

        when:
        transactions.forEach(repository.&save)
        def statisticsBeforeClear = repository.getAllStatistics()
        repository.clearTransactions()
        def statisticsAfterClear = repository.getAllStatistics()

        then:
        4 * configResolver.transactionsWindowInSeconds() >> 60
        statisticsBeforeClear.size() == 1
        statisticsAfterClear.size() == 0
    }
}
