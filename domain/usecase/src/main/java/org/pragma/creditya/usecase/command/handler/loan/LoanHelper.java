package org.pragma.creditya.usecase.command.handler.loan;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loan.bus.EventBus;
import org.pragma.creditya.model.loan.event.LoanEvent;
import org.pragma.creditya.model.loan.exception.LoanDomainException;
import org.pragma.creditya.model.loan.gateways.EventStoreRepository;
import org.pragma.creditya.model.loan.valueobject.LoanStatus;
import org.pragma.creditya.usecase.outbox.handler.IOutboxHandler;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class LoanHelper {

    private final EventStoreRepository eventRepository;
    private final EventBus eventBus;
    private final IOutboxHandler outboxProcess;

    public Mono<LoanStatus> checkDecisionType (String decision) {
        if (decision == null || decision.isBlank())
            return Mono.error(new LoanDomainException("Decision must be mandatory"));

        if (DecisionOptionsSingleton.getInstance().contains(LoanStatus.valueOf(decision)))
            return Mono.just(LoanStatus.valueOf(decision));

        return Mono.error(new LoanDomainException("Unknown decision type"));
    }

    public Mono<UUID> fromStringToUUID (String uuidString) {
        if (uuidString == null || uuidString.isBlank())
            return Mono.error(new LoanDomainException("Id must be provided"));

        try {
            UUID aggregateId = UUID.fromString(uuidString);
            return Mono.just(aggregateId).log();
        } catch (IllegalArgumentException ex) {
            return Mono.error(new LoanDomainException("Invalid UUID format"));
        }
    }

    public Mono<Loan> persistAndPublishEvents (Loan loan) {
        List<LoanEvent> events = loan.getUncommittedEvents();

        if (events.isEmpty())
            return Mono.just(loan);

        return eventRepository.saveAll(events)
                .then(outboxProcess.execute(loan))   // <- Se encadena aquí
                .thenMany(Flux.fromIterable(events).doOnNext(eventBus::publish))
                // .doOnSuccess(v -> outboxProcess.execute(loan))
                // .doOnSuccess(v -> events.forEach(eventBus::publish))
                .then(Mono.just(loan));
    }

    public Mono<Loan> getLoanById (UUID aggregateId) {
        return eventRepository.findByAggregateId(aggregateId)
                .collectList()
                .map(Loan::rehydrate);
    }

}
