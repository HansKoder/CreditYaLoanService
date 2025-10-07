package org.pragma.creditya.jobs.outbox.helper;

import org.pragma.creditya.model.loan.gateways.SQSProducer;
import org.pragma.creditya.usecase.outbox.LoanOutboxMessage;
import org.pragma.creditya.usecase.outbox.gateway.OutboxRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class SQSHelper {

    private final OutboxRepository outboxRepository;

    private final Logger logger = LoggerFactory.getLogger(SQSHelper.class);

    public SQSHelper(OutboxRepository outboxRepository) {
        this.outboxRepository = outboxRepository;
    }

    public Mono<Void> publish (SQSProducer sqsProducer, LoanOutboxMessage outbox) {
        logger.info("[infra.entrypoint.scheduler] (helper.publish) (01) process outbox payload=[ outbox:{} ]", outbox);

        UUID outboxId = outbox.getId();
        logger.info("[infra.entrypoint.scheduler] (helper.publish) (02) payload=[ outboxId:{} ]", outboxId);

        return sqsProducer.sendMessage(outbox.getPayload())
                .doOnSuccess(v -> logger.info("[infra.entrypoint.scheduler] (helper.publish) (03) message send, payload=[ outboxId:{}, message:{} ]", outboxId, outbox.getPayload()))
                .onErrorResume(err -> {
                    logger.info("[infra.entrypoint.scheduler] (helper.publish) (03) [ERROR] message was not send, payload=[ errorDetail:{} ]", err.getMessage());
                    return outboxRepository.markAFailed(outboxId);
                })
                .then(outboxRepository.markAsCompleted(outboxId));
    }

}
