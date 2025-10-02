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

        LoanRead read = LoanRead.builder()
                .loanId(event.getAggregateId())
                .amount(event.getAmount())
                .document(event.getDocument())
                .status(event.getStatus().name())
                .months(event.getPeriod())
                .typeLoan(event.getTypeLoan())
                .totalMonthlyDebt(event.getMonthlyDebt())
                .build();

        System.out.printf("[infra.event.handler] (onEvent) (3) LeanRead=%s", read);

        loanReadRepository.saveLoanRead(read)
                .doOnSuccess(v -> System.out.println("[infra.event.handler] (onEvent) (4) success event loanApplication submitted "))
                .doOnError(e -> System.out.println("[infra.event.handler] (onEvent) (4) error: " + e.getMessage()))
                .subscribe();
    }
}
