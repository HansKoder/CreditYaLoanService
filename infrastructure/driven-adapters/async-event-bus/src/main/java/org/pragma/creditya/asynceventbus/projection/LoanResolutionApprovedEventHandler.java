package org.pragma.creditya.asynceventbus.projection;

import org.pragma.creditya.model.loan.bus.EventBus;
import org.pragma.creditya.model.loan.bus.EventHandler;
import org.pragma.creditya.model.loan.event.LoanResolutionApprovedEvent;
import org.pragma.creditya.model.query.LoanRead;
import org.pragma.creditya.model.query.gateways.LoanReadRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class LoanResolutionApprovedEventHandler implements EventHandler<LoanResolutionApprovedEvent> {

    private final LoanReadRepository loanReadRepository;

    public LoanResolutionApprovedEventHandler(LoanReadRepository loanReadRepository, EventBus eventBus) {
        this.loanReadRepository = loanReadRepository;

        eventBus.subscribe(LoanResolutionApprovedEvent.class)
                .subscribe(this::onEvent);
    }

    @Override
    public void onEvent(LoanResolutionApprovedEvent event) {
        System.out.println("[infra.event.handler] (onEvent) (1) register a new event (approved Event), payload: " + event);

        loanReadRepository.getLoanByAggregateId(event.getAggregateId())
                .flatMap(loanRead -> mapToUpdateRead(loanRead, event))
                .flatMap(loanReadRepository::saveLoanRead)
                .doOnSuccess(v -> System.out.println("[infra.event.handler] (onEvent) (3) success event loan approved, Response=" + v))
                .doOnError(e -> System.out.println("[infra.event.handler] (onEvent) (3) loan approved was not persisted, Error=" + e.getMessage()))
                .subscribe();
    }

    private Mono<LoanRead> mapToUpdateRead (LoanRead loanRead, LoanResolutionApprovedEvent event) {
        System.out.println("[infra.event.mapping] map to update status ");
        loanRead.setStatus(event.getStatus().name());
        System.out.println("[infra.event.mapping] event was updated payload=" + event);
        return Mono.just(loanRead);
    }
}
