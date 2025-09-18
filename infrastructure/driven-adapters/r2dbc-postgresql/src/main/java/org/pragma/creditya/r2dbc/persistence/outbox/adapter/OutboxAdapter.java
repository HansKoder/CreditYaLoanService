package org.pragma.creditya.r2dbc.persistence.outbox.adapter;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.model.loan.event.LoanEvent;
import org.pragma.creditya.model.loan.gateways.OutboxRepository;
import org.pragma.creditya.r2dbc.persistence.outbox.repository.OutboxReactiveRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class OutboxAdapter implements OutboxRepository {

    private final OutboxReactiveRepository repository;

    @Override
    public Flux<LoanEvent> findByPending() {
        return null;
    }

    @Override
    public Mono<Void> markAsSent(UUID aggregateId) {
        return null;
    }

    @Override
    public Mono<Void> saveAll(List<LoanEvent> events) {
        return null;
    }
}
