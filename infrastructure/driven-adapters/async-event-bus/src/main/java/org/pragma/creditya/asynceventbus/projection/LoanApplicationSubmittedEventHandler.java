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
        System.out.println("[infra.event.handler] (onEvent) (1) register a new event, payload: " + event);

        System.out.printf("[infra.event.handler] (onEvent) (2) aggregateId=%s, doc=%s, amount=%s, typeLoan=%s, status=%s%n",
                event.getAggregateId(),
                event.getDocument(),
                event.getAmount(),
                event.getTypeLoan(),
                event.getStatus()
        );

        LoanRead read = LoanRead.builder()
                .loanId(event.getAggregateId())
                .amount(event.getAmount())
                .document(event.getDocument())
                .build();

        System.out.printf("[infra.event.handler] (onEvent) (3) LeanRead=%s", read);

        loanReadRepository.saveLoanRead(read)
                .doOnSuccess(v -> System.out.println("[infra.event.handler] (onEvent) (4) success event loanApplication submitted "))
                .doOnError(e -> System.out.println("[infra.event.handler] (onEvent) (4) error: " + e.getMessage()))
                .subscribe();
    }
}
