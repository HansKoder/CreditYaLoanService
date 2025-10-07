package org.pragma.creditya.usecase.outbox.gateway;

import org.pragma.creditya.usecase.outbox.LoanOutboxMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface OutboxRepository {

    <T> Mono<Void> saveOutboxMessage(LoanOutboxMessage outboxMessage, T payload);
    Flux<LoanOutboxMessage> findByPending ();
    Mono<Void> markAsCompleted (UUID outboxId);
    Mono<Void> markAFailed (UUID outboxId);

}
