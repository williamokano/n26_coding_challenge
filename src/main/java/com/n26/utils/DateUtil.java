package com.n26.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;

public abstract class DateUtil {
    public static long getEpochMillisFromLocalDateTime(LocalDateTime someDate) {
        return someDate.atZone(ZoneId.of("UTC")).toInstant().toEpochMilli();
    }

    public static long getEpochSecondsFromLocalDateTime(LocalDateTime someDate) {
        return secondsInMillis(getEpochMillisFromLocalDateTime(someDate));
    }

    public static long secondsInMillis(long millis) {
        return millis / 1000;
    }
}
