package org.pragma.creditya.jobs.outbox.strategy.handler;

import org.pragma.creditya.jobs.outbox.helper.SQSHelper;
import org.pragma.creditya.jobs.outbox.strategy.OutboxProcessStrategy;
import org.pragma.creditya.model.loan.gateways.SQSProducer;
import org.pragma.creditya.usecase.outbox.LoanOutboxMessage;
import org.pragma.creditya.usecase.outbox.OutboxTypeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class OutboxReportStrategy implements OutboxProcessStrategy {

    @Qualifier("SQSSenderNotificationAdapter")
    private final SQSProducer sqsProducer;

    private final SQSHelper sqsHelper;

    private final Logger logger = LoggerFactory.getLogger(OutboxReportStrategy.class);

    public OutboxReportStrategy(
            @Qualifier("SQSSenderReportAdapter") SQSProducer sqsProducer,
            SQSHelper sqsHelper
    ) {
        this.sqsHelper = sqsHelper;
        this.sqsProducer = sqsProducer;
    }

    @Override
    public Mono<Boolean> apply(LoanOutboxMessage outboxMessage) {
        logger.info("[infra.entrypoint.scheduler.report] (strategy) (apply-01) payload=[ outbox:{} ]", outboxMessage);
        return Mono.fromCallable(() -> shouldBeSentAnyQueue(outboxMessage))
                .doOnSuccess(response -> logger.info("[infra.entrypoint.scheduler.report] (strategy) (apply-02) response=[ isExecuted:{} ]", response));
    }

    private Boolean shouldBeSentAnyQueue (LoanOutboxMessage outboxMessage) {
        return outboxMessage.getType().equals(OutboxTypeEvent.REPORT);
    }

    @Override
    public Mono<Void> execute(LoanOutboxMessage outbox) {
        logger.info("[infra.entrypoint.scheduler.report] (strategy) (execute-01) payload=[ outbox:{} ]", outbox);
        return sqsHelper.publish(sqsProducer, outbox)
                .doOnSuccess(response -> logger.info("[infra.entrypoint.scheduler.report] (strategy) (execute-02) queue was executed with successful"));
    }
}
