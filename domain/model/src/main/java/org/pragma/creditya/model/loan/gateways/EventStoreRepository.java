package org.pragma.creditya.model.loan.gateways;

import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loan.event.LoanApplicationSubmittedEvent;
import org.pragma.creditya.model.loan.event.LoanEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface EventStoreRepository {
    Mono<Loan> findByAggregateIdLast (UUID aggregateId);
    Flux<LoanEvent> findByAggregateId (UUID aggregateId);
    Mono<Void> saveEvent (LoanApplicationSubmittedEvent event);
    Mono<Void> saveAll (List<LoanEvent> events);
}
