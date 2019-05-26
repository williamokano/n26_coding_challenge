package com.n26.service.impl

import com.n26.model.Statistics
import com.n26.repository.TransactionsRepository
import com.n26.utils.TestUtils
import spock.lang.Specification

class StatisticsServiceImplTest extends Specification {
    TransactionsRepository transactionsRepository = Mock(TransactionsRepository)
    StatisticsServiceImpl service = new StatisticsServiceImpl(transactionsRepository)

    def "should return empty when no data from repository"(String sum, String avg, String min, String max, Long count, List<Statistics> statistics) {
        when:
        def statistic = service.getStatistics()

        then:
        1 * transactionsRepository.getAllStatistics() >> statistics
        statistic.getSum() == new BigDecimal(sum)
        statistic.getAvg() == new BigDecimal(avg)
        statistic.getMin() == new BigDecimal(min)
        statistic.getMax() == new BigDecimal(max)
        statistic.getCount() == count

        where:
        sum      | avg     | min    | max     | count | statistics
        "0.00"   | "0.00"  | "0.00" | "0.00"  | 0     | []
        "300.00" | "27.27" | "0.00" | "20.00" | 11    | TestUtils.'build 2 statistics'()
        "11.00"  | "3.67"  | "3.00" | "5.00"  | 3     | TestUtils.'build 3 statistics'()
    }
}
