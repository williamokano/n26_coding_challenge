package com.n26.utils

import com.n26.model.Statistics
import com.n26.model.Transaction

import java.time.LocalDateTime

abstract class TestUtils {
    def static 'build 2 statistics'() {
        return [
                buildStatistic(100, 0, 10, 5),
                buildStatistic(200, 0, 20, 6)
        ]
    }

    def static 'build 3 statistics'() {
        return [
                buildStatistic(5, 5, 5, 1),
                buildStatistic(3, 3, 3, 1),
                buildStatistic(3, 3, 3, 1)
        ]
    }

    def static buildTransaction(String value) {
        return Transaction.builder()
                .amount(new BigDecimal(value))
                .timestamp(LocalDateTime.now())
                .build()
    }

    def static buildStatistic(Double sum, Double min, Double max, Long count) {
        return Statistics.builder()
                .sum(new BigDecimal(sum))
                .avg(BigDecimalUtil.avg(new BigDecimal(sum), count))
                .max(new BigDecimal(max))
                .min(new BigDecimal(min))
                .count(count)
                .build()
    }
}
