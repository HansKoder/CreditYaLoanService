package org.pragma.creditya.r2dbc.persistence.outbox;

import org.pragma.creditya.model.loan.gateways.OutboxRepository;
import org.pragma.creditya.model.loan.outbox.OutboxEventModel;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class OutboxAdapter implements OutboxRepository {
    @Override
    public Mono<Void> save(OutboxEventModel model) {
        return null;
    }

    @Override
    public Flux<OutboxEventModel> getPendingBatch(int size) {
        return null;
    }

    @Override
    public Mono<Void> clean() {
        return null;
    }

    @Override
    public Mono<Void> markSent(OutboxEventModel model) {
        return null;
    }
}
