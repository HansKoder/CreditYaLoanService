package org.pragma.creditya.sqs.sender;

import org.pragma.creditya.model.loan.gateways.SQSRepository;
import org.pragma.creditya.sqs.sender.config.SQSSenderProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pragma.creditya.sqs.sender.helper.SerializationHelper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

@Service
@Log4j2
@RequiredArgsConstructor
public class SQSSender implements SQSRepository {
    private final SQSSenderProperties properties;
    private final SqsAsyncClient client;
    private final SerializationHelper serializationHelper;

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
}
