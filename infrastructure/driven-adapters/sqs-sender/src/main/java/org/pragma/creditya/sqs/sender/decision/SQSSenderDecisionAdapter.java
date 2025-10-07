package org.pragma.creditya.sqs.sender.decision;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pragma.creditya.model.loan.gateways.SQSProducer;
import org.pragma.creditya.sqs.sender.decision.config.SQSSenderDecisionProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

@Service("SQSSenderDecisionAdapter")
@Log4j2
public class SQSSenderDecisionAdapter implements SQSProducer {
    private final SQSSenderDecisionProperties properties;

    @Qualifier("decisionSqsClient")
    private final SqsAsyncClient client;

    private final Logger logger = LoggerFactory.getLogger(SQSSenderDecisionAdapter.class);

    public SQSSenderDecisionAdapter(SQSSenderDecisionProperties properties, @Qualifier("decisionSqsClient") SqsAsyncClient client) {
        this.properties = properties;
        this.client = client;
    }

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
    public Mono<Void> sendMessage(String payload) {
        logger.info("[infra.sqs-sender.decision] (producer) (step-1) send message, payload=[ payload:{} ]", payload);
        return this.send(payload)
                .doOnSuccess(messageId -> log.info("[infra.sqs-sender.decision] (producer) (step-2) message was sent, response=[  messageId:{} ]", messageId))
                .doOnError(err -> log.error("[infra.sqs-sender.decision] [ERROR] (producer) (step-2) it was not impossible to send message, response=[  errorDetail:{} ]", err.getMessage()))
                .then();
    }
}
