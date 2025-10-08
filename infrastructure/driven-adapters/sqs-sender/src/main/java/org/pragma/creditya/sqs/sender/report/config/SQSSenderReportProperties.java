package org.pragma.creditya.sqs.sender.report.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "adapter.sqs.report")
public record SQSSenderReportProperties(
     String region,
     String queueUrl,
     String endpoint){
}
