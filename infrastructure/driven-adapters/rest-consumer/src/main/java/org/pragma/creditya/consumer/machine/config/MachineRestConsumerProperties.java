package org.pragma.creditya.consumer.machine.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "machine.client")
public record MachineRestConsumerProperties (
        String id,
        String secret
) { }
