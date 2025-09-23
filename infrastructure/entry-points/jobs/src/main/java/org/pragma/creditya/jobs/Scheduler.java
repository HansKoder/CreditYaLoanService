package org.pragma.creditya.jobs;

import org.pragma.creditya.model.loan.gateways.OutboxRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Component
public class Scheduler {

    // tasks
    // Job run each 60 seconds - print running job outbox
    // Repository outbox by started findAll (status=started)
    // If return any data process ->
    // Implements use case specifics for handling each outbox scenario like (notification, auto-decision)
    // Use case - notification steps
    //            - consumer service customer - build payload
    //            - implement sqs for sending event.
    // Check - mark as sent (repository - outbox)

    private final OutboxRepository outboxRepository;

    public Scheduler(OutboxRepository outboxRepository) {
        this.outboxRepository = outboxRepository;
        startJob();
    }

    private void startJob() {
        int TIME_PROCESS_OUTBOX = 1;
        Flux.interval(Duration.ofMinutes(TIME_PROCESS_OUTBOX))
                .flatMap(tick -> outboxRepository.findByPending())
                .subscribe();
    }

}
