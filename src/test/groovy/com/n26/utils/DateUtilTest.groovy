package com.n26.utils

import spock.lang.Specification

import java.time.LocalDateTime
import java.time.Month

class DateUtilTest extends Specification {
    def "should convert LocalDateTime to epochMilis"() {
        given:
        LocalDateTime localDateTime = LocalDateTime.of(1989, Month.MARCH, 15, 4, 37, 46)

        when:
        long expectedMilli = 605950666000L

        then:
        expectedMilli == DateUtil.getEpochFromLocalDate(localDateTime)
    }
}
