package org.pragma.creditya.jobs.outbox.strategy;

import org.pragma.creditya.usecase.outbox.LoanOutboxMessage;
import reactor.core.publisher.Mono;

public interface OutboxProcessStrategy {
    Mono<Boolean> apply(LoanOutboxMessage outboxMessage);
    Mono<Void> execute (LoanOutboxMessage outboxMessage);
}
