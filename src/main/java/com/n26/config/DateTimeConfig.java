package com.n26.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@Configuration
public class DateTimeConfig {

    private Logger logger = LoggerFactory.getLogger(DateTimeConfig.class);

    @PostConstruct
    void init() {
        logger.info("Setting timezone as UTC");
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
}
