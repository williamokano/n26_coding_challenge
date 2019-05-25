package com.n26.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;

public abstract class DateUtil {
    public static long getEpochFromLocalDate(LocalDateTime someDate) {
        return someDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
}
