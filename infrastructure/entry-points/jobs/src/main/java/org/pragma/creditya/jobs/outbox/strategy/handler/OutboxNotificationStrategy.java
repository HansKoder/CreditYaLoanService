package org.pragma.creditya.jobs.outbox.strategy.handler;

import org.pragma.creditya.jobs.outbox.strategy.OutboxProcessStrategy;
import org.pragma.creditya.jobs.outbox.helper.SQSHelper;
import org.pragma.creditya.model.loan.gateways.SQSProducer;
import org.pragma.creditya.usecase.outbox.LoanOutboxMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static org.pragma.creditya.jobs.outbox.constant.OutboxConstant.*;


@Component
public class OutboxNotificationStrategy implements OutboxProcessStrategy {

    @Qualifier("SQSSenderNotificationAdapter")
    private final SQSProducer sqsProducer;

    private final SQSHelper sqsHelper;

    private final Logger logger = LoggerFactory.getLogger(OutboxNotificationStrategy.class);

    public OutboxNotificationStrategy(@Qualifier("SQSSenderNotificationAdapter") SQSProducer sqsProducer, SQSHelper sqsHelper) {
        this.sqsProducer = sqsProducer;
        this.sqsHelper = sqsHelper;
    }

    @Override
    public Mono<Boolean> apply(LoanOutboxMessage outboxMessage) {
        logger.info("[infra.entrypoint.scheduler.notification] (strategy) (apply-01) payload=[ outbox:{} ]", outboxMessage);
        return Mono.fromCallable(() -> shouldBeSentAnyQueue(outboxMessage))
                .doOnSuccess(response -> logger.info("[infra.entrypoint.scheduler.notification] (strategy) (apply-02) response=[ isExecuted:{} ]", response));
    }

    private Boolean shouldBeSentAnyQueue (LoanOutboxMessage outboxMessage) {
        return outboxMessage.getType().equals(APPROVED_EVENT) || outboxMessage.getType().equals(REJECTED_EVENT);
    }

    @Override
    public Mono<Void> execute(LoanOutboxMessage outbox) {
        logger.info("[infra.entrypoint.scheduler.notification] (strategy) (execute-01) payload=[ outbox:{} ]", outbox);
        return sqsHelper.publish(sqsProducer, outbox)
                .doOnSuccess(response -> logger.info("[infra.entrypoint.scheduler.notification] (strategy) (execute-02) queue was executed with successful"));
    }
}
