package org.pragma.creditya.sqs.sender.notification.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "adapter.sqs.notification")
public record SQSSenderNotificationProperties(
     String region,
     String queueUrl,
     String endpoint){
}
