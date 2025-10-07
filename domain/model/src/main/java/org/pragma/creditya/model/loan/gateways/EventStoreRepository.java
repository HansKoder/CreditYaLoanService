package org.pragma.creditya.model.loan.gateways;

import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loan.event.LoanEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface EventStoreRepository {
    Flux<LoanEvent> findByAggregateId (UUID aggregateId);
    Mono<Void> saveAll (List<LoanEvent> events);
}
