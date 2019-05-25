package com.n26.controller;

import com.n26.api.StatisticsResource;
import com.n26.api.response.StatisticsResponse;
import com.n26.service.StatisticsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatisticsController implements StatisticsResource {

    private final StatisticsService statisticsService;
    private final ModelMapper modelMapper;

    @Autowired
    public StatisticsController(StatisticsService statisticsService, ModelMapper modelMapper) {
        this.statisticsService = statisticsService;
        this.modelMapper = modelMapper;
    }

    @Override
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public StatisticsResponse getStatistics() {
        return modelMapper.map(statisticsService.getStatistics(), StatisticsResponse.class);
    }
}
