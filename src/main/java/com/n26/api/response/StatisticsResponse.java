package com.n26.api.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;

import java.math.BigDecimal;

@Log
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsResponse {
    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal sum;

    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal avg;

    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal max;

    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal min;

    private Long count;
}
