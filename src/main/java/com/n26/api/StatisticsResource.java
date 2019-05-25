package com.n26.api;

import com.n26.api.response.StatisticsResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = "/statistics")
public interface StatisticsResource {
    @GetMapping
    StatisticsResponse getStatistics();
}
