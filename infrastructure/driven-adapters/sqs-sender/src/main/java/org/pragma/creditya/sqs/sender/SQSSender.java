package org.pragma.creditya.sqs.sender;

import org.pragma.creditya.model.loan.gateways.SQSProducer;
import org.pragma.creditya.sqs.sender.config.SQSSenderProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pragma.creditya.sqs.sender.helper.SerializationHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

@Service
@Log4j2
@RequiredArgsConstructor
public class SQSSender implements SQSProducer {
    private final SQSSenderProperties properties;
    private final SqsAsyncClient client;
    private final SerializationHelper serializationHelper;

    private final Logger logger = LoggerFactory.getLogger(SQSSender.class);

    public Mono<String> send(String message) {
        return Mono.fromCallable(() -> buildRequest(message))
                .flatMap(request -> Mono.fromFuture(client.sendMessage(request)))
                .doOnNext(response -> log.debug("Message sent {}", response.messageId()))
                .map(SendMessageResponse::messageId);
    }

    private SendMessageRequest buildRequest(String message) {
        return SendMessageRequest.builder()
                .queueUrl(properties.queueUrl())
                .messageBody(message)
                .build();
    }


    @Override
    public Mono<Void> sendMessage(Object payload) {
        return Mono.fromCallable(() -> serializationHelper.serialize(payload))
                .flatMap(this::send)
                .then();
    }

    @Override
    public Mono<Void> sendMessage(String payload) {
        logger.info("[infra.sqs-sender] (producer) (step-1) send message, payload=[ payload:{} ]", payload);
        return this.send(payload)
                .doOnSuccess(messageId -> log.info("[infra.sqs-sender] (producer) (step-2) message was sent, response=[  messageId:{} ]", messageId))
                .doOnError(err -> log.error("[infra.sqs-sender] [ERROR] (producer) (step-2) it was not impossible to send message, response=[  errorDetail:{} ]", err.getMessage()))
                .then();
    }
}
