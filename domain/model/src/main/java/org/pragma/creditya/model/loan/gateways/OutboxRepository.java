package org.pragma.creditya.model.loan.gateways;

import org.pragma.creditya.model.loan.event.LoanEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface OutboxRepository {
    Flux<LoanEvent> findByPending ();
    Mono<Void> markAsCompleted (UUID outboxId);
    Mono<Void> markAFailed (UUID outboxId);
    Mono<Void> saveAll (List<LoanEvent> events);
}
