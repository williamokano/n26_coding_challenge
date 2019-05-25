package com.n26.service.impl

import com.n26.model.Transaction
import com.n26.repository.TransactionsRepository
import com.n26.utils.TestUtils
import spock.lang.Specification

class StatisticServiceImplTest extends Specification {
    TransactionsRepository transactionsRepository = Mock(TransactionsRepository)
    StatisticsServiceImpl service = new StatisticsServiceImpl(transactionsRepository)

    def "should return empty when no data from repository"(String sum, String avg, String min, String max, Long count, List<Transaction> transactions) {
        when:
        def statistic = service.getStatistics()

        then:
        1 * transactionsRepository.findAll() >> transactions
        statistic.sum == new BigDecimal(sum)
        statistic.avg == new BigDecimal(avg)
        statistic.min == new BigDecimal(min)
        statistic.max == new BigDecimal(max)
        statistic.count == count

        where:
        sum     | avg    | min    | max    | count | transactions
        "0.00"  | "0.00" | "0.00" | "0.00" | 0     | []
        "8.00"  | "4.00" | "3.00" | "5.00" | 2     | TestUtils."build 2 transactions"()
        "11.00" | "3.67" | "3.00" | "5.00" | 3     | TestUtils."build 3 transactions"()
    }
}
