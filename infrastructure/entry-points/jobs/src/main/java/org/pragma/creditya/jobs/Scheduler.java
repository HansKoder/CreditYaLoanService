package org.pragma.creditya.jobs;

import org.pragma.creditya.jobs.outbox.strategy.OutboxStrategyDispatcher;
import org.pragma.creditya.usecase.outbox.gateway.OutboxRepository;
import org.pragma.creditya.usecase.outbox.LoanOutboxMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class Scheduler {

    private final OutboxRepository outboxRepository;

    private final OutboxStrategyDispatcher dispatcher;

    private static final int TIME_PROCESS_OUTBOX = 20; // seconds
    private final Logger logger = LoggerFactory.getLogger(Scheduler.class);

    public Scheduler(OutboxRepository outboxRepository, OutboxStrategyDispatcher dispatcher) {
        this.outboxRepository = outboxRepository;
        this.dispatcher = dispatcher;
        startJob();
    }

    private void startJob() {
        logger.info("[infra.entrypoint.scheduler] (startJob) Scheduler Outbox initialized, payload=[ time (seconds): {} ]", TIME_PROCESS_OUTBOX);

        Flux.interval(Duration.ofSeconds(TIME_PROCESS_OUTBOX))
                .flatMap(tick -> processPendingOutboxMessages()
                        .onErrorContinue((err, obj) ->
                                logger.error("[infra.entrypoint.scheduler] Error processing outbox item={}, err={}", obj, err.getMessage(), err))
                )
                .subscribe(
                        unused -> {}, // handled within pipeline
                        err -> logger.error("[infra.entrypoint.scheduler] Stream failed unexpectedly", err),
                        () -> logger.info("[infra.entrypoint.scheduler] Scheduler stream completed (unexpected).")
                );
    }

    private Flux<Void> processPendingOutboxMessages() {
        logger.info("[infra.entrypoint.scheduler] (processPendingOutboxMessages) Fetching pending outbox messages...");

        return outboxRepository.findByPending()
                .flatMap(this::dispatchSafely)
                .doOnComplete(() -> logger.info("[infra.entrypoint.scheduler] (processPendingOutboxMessages) Completed iteration successfully"));
    }

    private Mono<Void> dispatchSafely(LoanOutboxMessage outbox) {
        logger.info("[infra.entrypoint.scheduler] (dispatchSafely) Processing outbox payload=[ {} ]", outbox);

        return dispatcher.dispatch(outbox)
                .doOnSuccess(v -> logger.info("[infra.entrypoint.scheduler] (dispatchSafely) Outbox processed successfully, id=[ {} ]", outbox.getId()))
                .onErrorResume(err -> {
                    logger.error("[infra.entrypoint.scheduler] (dispatchSafely) [ERROR] Failed processing outbox id=[ {} ], detail=[ {} ]", outbox.getId(), err.getMessage());
                    return outboxRepository.markAFailed(outbox.getId());
                });
    }

}
