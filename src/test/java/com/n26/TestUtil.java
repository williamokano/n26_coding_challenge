package com.n26;

import com.n26.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public abstract class TestUtil {
    public static Transaction transaction(String value) {
        return Transaction.builder()
                .amount(new BigDecimal(value))
                .timestamp(LocalDateTime.now())
                .build();
    }
}
