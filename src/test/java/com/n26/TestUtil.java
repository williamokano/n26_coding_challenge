package com.n26;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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

    public static String objectToString(Object any) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return objectMapper.writeValueAsString(any);
    }
}
