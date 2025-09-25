package org.pragma.creditya.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(basePackages = "org.pragma.creditya.outbox",
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "^.+Handler$")
        },
        useDefaultFilters = false)
public class OutboxHandlerConfig {
}
