package com.n26.utils

import com.n26.model.Statistics
import com.n26.model.Transaction
import spock.lang.Specification

import java.time.LocalDateTime

class StatisticsUtilTest extends Specification {

    def "should update statistic min if transaction amount is lesser"(String transactionAmout, String statisticMin, String expected) {
        given:
        Transaction transaction = Transaction.builder()
                .amount(new BigDecimal(transactionAmout))
                .timestamp(LocalDateTime.now())
                .build()
        Statistics statistic = Statistics.builder()
                .min(new BigDecimal(statisticMin))
                .build()

        when:
        StatisticUtil.updateStatisticMinIfTransactionIsLesser(statistic, transaction)

        then:
        statistic.getMin() <= transaction.getAmount()
        statistic.getMin() == new BigDecimal(expected)

        where:
        transactionAmout | statisticMin | expected
        "100"            | "100"        | "100"
        "100"            | "200"        | "100"
        "200"            | "100"        | "100"
        "-50"            | "100"        | "-50"
        "-50"            | "-70"        | "-70"
        "0"              | "100"        | "0"
        "0"              | "-100"       | "-100"
    }

    def "should update statistic max if transaction amount is greater"(String transactionAmout, String statisticMax, String expected) {
        given:
        Transaction transaction = Transaction.builder()
                .amount(new BigDecimal(transactionAmout))
                .timestamp(LocalDateTime.now())
                .build()
        Statistics statistic = Statistics.builder()
                .max(new BigDecimal(statisticMax))
                .build()

        when:
        StatisticUtil.updateStatisticMaxIfTransactionIsGreater(statistic, transaction)

        then:
        statistic.getMax() >= transaction.getAmount()
        statistic.getMax() == new BigDecimal(expected)

        where:
        transactionAmout | statisticMax | expected
        "100"            | "100"        | "100"
        "100"            | "200"        | "200"
        "200"            | "100"        | "200"
        "-50"            | "100"        | "100"
        "-50"            | "-70"        | "-50"
        "0"              | "100"        | "100"
        "0"              | "-100"       | "0"
    }

}
