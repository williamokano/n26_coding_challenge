package com.n26.controller

import com.n26.api.response.StatisticsResponse
import com.n26.model.Statistic
import com.n26.service.StatisticsService
import org.modelmapper.ModelMapper
import spock.lang.Specification

class StatisticsControllerTest extends Specification {
    StatisticsService statisticsService = Mock(StatisticsService)
    ModelMapper modelMapper = Mock(ModelMapper)
    StatisticsController controller = new StatisticsController(statisticsService, modelMapper)

    def "should get statistics"() {
        given:
        Statistic statistic = buildStatistic()
        StatisticsResponse statisticResponse = buildStatisticResponse()

        when:
        StatisticsResponse response = controller.getStatistics()

        then:
        1 * statisticsService.getStatistics() >> statistic
        1 * modelMapper.map(statistic, StatisticsResponse.class) >> statisticResponse
        response.getSum() == statistic.getSum()
        response.getAvg() == statistic.getAvg()
        response.getMin() == statistic.getMin()
        response.getMax() == statistic.getMax()
        response.getCount() == statistic.getCount()
    }

    def buildStatistic() {
        return Statistic.builder()
                .sum(new BigDecimal("10"))
                .avg(new BigDecimal("10"))
                .min(new BigDecimal("10"))
                .max(new BigDecimal("10"))
                .count(1L)
                .build()
    }

    def buildStatisticResponse() {
        return StatisticsResponse.builder()
                .sum(new BigDecimal("10"))
                .avg(new BigDecimal("10"))
                .min(new BigDecimal("10"))
                .max(new BigDecimal("10"))
                .count(1L)
                .build()
    }
}
