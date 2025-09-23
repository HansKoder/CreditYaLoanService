package org.pragma.creditya.jobs;

import org.pragma.creditya.model.loan.event.LoanEvent;
import org.pragma.creditya.model.loan.gateways.OutboxRepository;
import org.pragma.creditya.usecase.IOrchestratorUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.UUID;

@Component
public class Scheduler {

    private final OutboxRepository outboxRepository;
    private final IOrchestratorUseCase useCase;
    private final Logger logger = LoggerFactory.getLogger(Scheduler.class);

    public Scheduler(OutboxRepository outboxRepository, IOrchestratorUseCase useCase) {
        this.outboxRepository = outboxRepository;
        this.useCase = useCase;
        startJob();
    }


    private void startJob() {
        int TIME_PROCESS_OUTBOX = 30;
        logger.info("[infra.entrypoint.scheduler] Running Scheduler Outbox, Payload=[ time (seconds) :{} ]", TIME_PROCESS_OUTBOX);

        Flux.interval(Duration.ofSeconds(TIME_PROCESS_OUTBOX))
                .flatMap(tick ->
                        outboxRepository.findByPending()
                                .flatMap(this::processOutbox)
                                .onErrorContinue((err, obj) -> logger.error("[infra.entrypoint.scheduler] Error processing item {}, err={}", obj, err.getMessage(), err))
                )
                .subscribe(
                        unused -> { },
                        err -> logger.error("[infra.entrypoint.scheduler] Stream failed unexpectedly", err)
                );
    }

    private Mono<Void> processOutbox (LoanEvent outbox) {
        logger.info("[infra.entrypoint.scheduler] process outbox payload=[ outbox:{} ]", outbox);

        UUID outboxId = outbox.getId();

        return useCase.outboxProcess(outbox)
                .retryWhen(Retry.backoff(1, Duration.ofSeconds(5))
                        .doBeforeRetry(rs -> logger.warn("[infra.entrypoint.scheduler] Retrying outboxId={} attempt={} cause={}",
                                outboxId, rs.totalRetriesInARow() + 1, rs.failure().getMessage())))
                .then(outboxRepository.markAsCompleted(outboxId))
                .onErrorResume(err -> {
                    logger.error("[infra.entrypoint.scheduler] Outbox should be marked as FAILED, id={}, error={}", outboxId, err.getMessage(), err);
                    return outboxRepository.markAFailed(outboxId)
                            .doOnError(e -> logger.error("[infra.entrypoint.scheduler] markAFailed also failed for id={}, err={}", outboxId, e.getMessage(), e))
                            .then();
                });
    }

}
