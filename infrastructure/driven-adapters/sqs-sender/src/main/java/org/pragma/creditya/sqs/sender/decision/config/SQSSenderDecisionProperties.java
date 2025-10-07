package org.pragma.creditya.sqs.sender.decision.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "adapter.sqs.decision")
public record SQSSenderDecisionProperties(
     String region,
     String queueUrl,
     String endpoint){
}
