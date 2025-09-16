package org.pragma.creditya.asynceventbus.projection;

import org.pragma.creditya.model.loan.bus.EventHandler;
import org.pragma.creditya.model.loan.event.LoanResolutionApprovedEvent;
import org.pragma.creditya.model.loanread.LoanRead;
import org.pragma.creditya.model.loanread.gateways.LoanReadRepository;
import org.springframework.stereotype.Component;

@Component
public class LoanResolutionApprovedEventHandler implements EventHandler<LoanResolutionApprovedEvent> {

    private final LoanReadRepository loanReadRepository;

    public LoanResolutionApprovedEventHandler(LoanReadRepository loanReadRepository) {
        this.loanReadRepository = loanReadRepository;
    }

    @Override
    public void onEvent(LoanResolutionApprovedEvent event) {
        System.out.println("[infra.event.handler] (onEvent) (1) register a new event, payload: " + event);

        loanReadRepository.getLoanByAggregateId(event.getAggregateId())
                .map(loanRead -> mapToUpdateRead(loanRead, event))
                .flatMap(loanReadRepository::saveLoanRead)
                .doOnSuccess(v -> System.out.println("[infra.event.handler] (onEvent) (3) success event loan approved"))
                .doOnError(e -> System.out.println("[infra.event.handler] (onEvent) (3) loan approved was not persisted, Error=" + e.getMessage()))
                .subscribe();
    }

    private LoanRead mapToUpdateRead (LoanRead loanRead, LoanResolutionApprovedEvent event) {
        loanRead.setStatus(event.getStatus());
        return loanRead;
    }
}
