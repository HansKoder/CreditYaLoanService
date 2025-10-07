package org.pragma.creditya.asynceventbus.projection.strategy.handler;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.asynceventbus.projection.strategy.ProjectionStrategy;
import org.pragma.creditya.model.loan.event.payload.ApplicationRejectedEvent;
import org.pragma.creditya.model.loan.event.LoanEvent;
import org.pragma.creditya.usecase.query.handler.loan.dto.LoanSummaryDTO;
import org.pragma.creditya.usecase.query.repository.LoanReadRepository;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RejectedEventProjection implements ProjectionStrategy {

    private final LoanReadRepository loanReadRepository;

    @Override
    public Boolean shouldBeExecuted(LoanEvent event) {
        return event.getPayload() instanceof ApplicationRejectedEvent;
    }

    @Override
    public void replicate(LoanEvent event) {
        System.out.println("[infra.bus.strategy] (loan.strategy), rejected event, payload=[ event:{" + event + "} ]");

        var payload = (ApplicationRejectedEvent) event.getPayload();

        loanReadRepository.getLoanByAggregateId(event.getAggregateId())
                .map(loanRead -> mapToUpdateRead(loanRead, payload))
                .flatMap(loanReadRepository::saveLoanRead)
                .doOnSuccess(v -> System.out.println("[infra.event.handler] (onEvent) (3) success event loan rejected"))
                .doOnError(e -> System.out.println("[infra.event.handler] (onEvent) (3) loan rejected was not persisted, Error=" + e.getMessage()))
                .subscribe();
    }

    private LoanSummaryDTO mapToUpdateRead (LoanSummaryDTO loanRead, ApplicationRejectedEvent event) {
        loanRead.setStatus(event.getStatus().name());
        return loanRead;
    }
}
