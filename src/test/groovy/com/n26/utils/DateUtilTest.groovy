package com.n26.utils

import spock.lang.Specification

import java.time.LocalDateTime
import java.time.Month

class DateUtilTest extends Specification {

    def "setup"() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    }

    def "should convert LocalDateTime to epochMilis"() {
        given:
        LocalDateTime localDateTime = LocalDateTime.of(1989, Month.MARCH, 15, 4, 37, 46)

        when:
        long expectedMilli = 605939866000L

        then:
        expectedMilli == DateUtil.getEpochMillisFromLocalDateTime(localDateTime)
    }

    def "should convert LocalDateTime to epoch seconds"() {
        given:
        LocalDateTime localDateTime = LocalDateTime.of(1989, Month.MARCH, 15, 4, 37, 46)

        when:
        long expectedMilli = 605939866L

        then:
        expectedMilli == DateUtil.getEpochSecondsFromLocalDateTime(localDateTime)
    }

    def "should convert millis to seconds"(long millis, long seconds) {
        expect:
        seconds == DateUtil.secondsInMillis(millis)

        where:
        millis | seconds
        60000  | 60
        60001  | 60
        60999  | 60
        60654  | 60
        70000  | 70
        70999  | 70
        70001  | 70
        70690  | 70
    }
}
