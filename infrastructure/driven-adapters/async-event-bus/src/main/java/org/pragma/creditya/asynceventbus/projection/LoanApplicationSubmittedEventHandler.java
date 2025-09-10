package org.pragma.creditya.asynceventbus.projection;

import org.pragma.creditya.model.loan.bus.EventBus;
import org.pragma.creditya.model.loan.bus.EventHandler;
import org.pragma.creditya.model.loan.event.LoanApplicationSubmittedEvent;
import org.pragma.creditya.model.loanread.LoanRead;
import org.pragma.creditya.model.loanread.gateways.LoanReadRepository;
import org.springframework.stereotype.Component;

@Component
public class LoanApplicationSubmittedEventHandler implements EventHandler<LoanApplicationSubmittedEvent> {

    private final LoanReadRepository loanReadRepository;

    public LoanApplicationSubmittedEventHandler(LoanReadRepository loanReadRepository, EventBus eventBus) {
        this.loanReadRepository = loanReadRepository;

        eventBus.subscribe(LoanApplicationSubmittedEvent.class)
                .subscribe(this::onEvent);
    }

    @Override
    public void onEvent(LoanApplicationSubmittedEvent event) {
        System.out.println("[infra.event.handler] (onEvent) register a new event, payload: " + event);

        LoanRead read = LoanRead.builder()
                .loanId(event.getAggregateId())
                .amount(event.getAmount())
                .document(event.getDocument())
                .build();

        loanReadRepository.saveLoanRead(read)
                .doOnSuccess(v -> System.out.println("[infra.event.handler] (onEvent) success event loanApplication submitted "))
                .doOnError(e -> System.out.println("[infra.event.handler] (onEvent) error: " + e.getMessage()))
                .subscribe();
    }
}
