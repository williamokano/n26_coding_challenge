package com.n26.utils

import com.n26.model.Transaction

import java.time.LocalDateTime

abstract class TestUtils {
    def static "build 2 transactions"() {
        return [
                buildTransaction("5"),
                buildTransaction("3")
        ]
    }

    def static "build 3 transactions"() {
        return [
                buildTransaction("5"),
                buildTransaction("3"),
                buildTransaction("3")
        ]
    }

    def static buildTransaction(String value) {
        return Transaction.builder()
                .amount(new BigDecimal(value))
                .timestamp(LocalDateTime.now())
                .build()
    }
}
