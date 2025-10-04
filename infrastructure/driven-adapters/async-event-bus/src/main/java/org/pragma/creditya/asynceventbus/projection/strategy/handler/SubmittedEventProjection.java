package org.pragma.creditya.asynceventbus.projection.strategy.handler;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.asynceventbus.projection.strategy.ProjectionStrategy;
import org.pragma.creditya.model.loan.event.ApplicationSubmittedEvent;
import org.pragma.creditya.model.loan.event.LoanEvent;
import org.pragma.creditya.usecase.query.handler.loan.dto.LoanSummaryDTO;
import org.pragma.creditya.usecase.query.repository.LoanReadRepository;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SubmittedEventProjection implements ProjectionStrategy {

    private final LoanReadRepository loanReadRepository;

    @Override
    public Boolean shouldBeExecuted(LoanEvent event) {
        return event.getPayload() instanceof ApplicationSubmittedEvent;
    }

    @Override
    public void replicate(LoanEvent event) {
        System.out.println("[infra.bus.strategy] (loan.strategy), submitted event, payload=[ event:{" + event + "} ]");

        var payload = (ApplicationSubmittedEvent) event.getPayload();

        LoanSummaryDTO read = LoanSummaryDTO.builder()
                .loanId(event.getAggregateId())
                .amount(payload.getAmount())
                .document(payload.getDocument())
                .status(payload.getStatus().name())
                .months(payload.getPeriod())
                .typeLoan(payload.getTypeLoan())
                .totalMonthlyDebt(payload.getMonthlyDebt())
                .build();

        System.out.println("[infra.bus.strategy] (loan.strategy), submitted event, response=[ loanRead:{" + read + "} ]");

        loanReadRepository.saveLoanRead(read)
                .doOnSuccess(v -> System.out.println("[infra.event.handler] (onEvent) (4) success event loanApplication submitted "))
                .doOnError(e -> System.out.println("[infra.event.handler] (onEvent) (4) error: " + e.getMessage()))
                .subscribe();
    }
}
