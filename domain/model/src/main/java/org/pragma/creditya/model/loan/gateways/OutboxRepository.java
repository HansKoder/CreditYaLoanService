package org.pragma.creditya.model.loan.gateways;

import org.pragma.creditya.model.loan.outbox.OutboxEventModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface OutboxRepository {

    Mono<Void> save(OutboxEventModel model);
    Flux<OutboxEventModel> getPendingBatch (int size);
    Mono<Void> clean ();
    Mono<Void> markSent (OutboxEventModel model);
}
