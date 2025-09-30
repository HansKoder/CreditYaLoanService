package org.pragma.creditya.model.loan.gateways;

import org.pragma.creditya.model.loan.event.LoanEvent;
import org.pragma.creditya.model.outbox.LoanOutboxMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface OutboxRepository {

    <T> Mono<Void> saveOutboxMessage(LoanOutboxMessage outboxMessage, T payload);
    Flux<LoanOutboxMessage> findByPending ();
    Mono<Void> markAsCompleted (UUID outboxId);
    Mono<Void> markAFailed (UUID outboxId);

}
