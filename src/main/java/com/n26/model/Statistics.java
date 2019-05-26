package com.n26.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Statistics {
    private BigDecimal sum;
    private BigDecimal avg;
    private BigDecimal max;
    private BigDecimal min;
    private Long count;

    public Statistics roundHalfUp() {
        sum = sum.setScale(2, BigDecimal.ROUND_HALF_UP);
        avg = avg.setScale(2, BigDecimal.ROUND_HALF_UP);
        max = max.setScale(2, BigDecimal.ROUND_HALF_UP);
        min = min.setScale(2, BigDecimal.ROUND_HALF_UP);
        return this;
    }

    public static Statistics empty() {
        return Statistics.builder()
                .sum(new BigDecimal(0))
                .avg(new BigDecimal(0))
                .min(new BigDecimal(0))
                .max(new BigDecimal(0))
                .count(0L)
                .build();
    }
}
