package com.n26.controller;

import com.n26.api.StatisticsResource;
import com.n26.api.response.StatisticsResponse;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class StatisticsController implements StatisticsResource {
    @Override
    @ResponseBody
    public StatisticsResponse getStatistics() {
        return StatisticsResponse.builder()
                .sum(BigDecimal.ZERO)
                .avg(BigDecimal.ZERO)
                .max(BigDecimal.ZERO)
                .min(BigDecimal.ZERO)
                .count(0L)
                .build();
    }
}
