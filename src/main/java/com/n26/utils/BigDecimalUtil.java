package com.n26.utils;

import java.math.BigDecimal;

public abstract class BigDecimalUtil {
    public static BigDecimal max(BigDecimal target, BigDecimal other) {
        return target.compareTo(other) > 0 ? target : other;
    }

    public static BigDecimal min(BigDecimal target, BigDecimal other) {
        return target.compareTo(other) < 0 ? target : other;
    }

    public static BigDecimal avg(BigDecimal value, Long count) {
        return new BigDecimal(value.doubleValue() / count);
    }
}
