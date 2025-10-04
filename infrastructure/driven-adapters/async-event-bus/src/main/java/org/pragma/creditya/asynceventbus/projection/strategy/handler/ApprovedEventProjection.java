package org.pragma.creditya.asynceventbus.projection.strategy.handler;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.asynceventbus.projection.strategy.ProjectionStrategy;
import org.pragma.creditya.model.loan.event.payload.ApplicationApprovedEvent;
import org.pragma.creditya.model.loan.event.LoanEvent;
import org.pragma.creditya.usecase.query.handler.loan.dto.LoanSummaryDTO;
import org.pragma.creditya.usecase.query.repository.LoanReadRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class ApprovedEventProjection implements ProjectionStrategy {

    private final LoanReadRepository loanReadRepository;

    @Override
    public Boolean shouldBeExecuted(LoanEvent event) {
        return event.getPayload() instanceof ApplicationApprovedEvent;
    }

    @Override
    public void replicate(LoanEvent event) {
        System.out.println("[infra.bus.strategy] (loan.strategy), approved event, payload=[ event:{" + event + "} ]");

        var payload = (ApplicationApprovedEvent) event.getPayload();

        loanReadRepository.getLoanByAggregateId(event.getAggregateId())
                .flatMap(loanRead -> mapToUpdateRead(loanRead, payload))
                .flatMap(loanReadRepository::saveLoanRead)
                .doOnSuccess(v -> System.out.println("[infra.event.handler] (onEvent) (3) success event loan approved, Response=" + v))
                .doOnError(e -> System.out.println("[infra.event.handler] (onEvent) (3) loan approved was not persisted, Error=" + e.getMessage()))
                .subscribe();
    }

    private Mono<LoanSummaryDTO> mapToUpdateRead (LoanSummaryDTO loanRead, ApplicationApprovedEvent event) {
        System.out.println("[infra.event.mapping] map to update status ");
        loanRead.setStatus(event.getStatus().name());
        System.out.println("[infra.event.mapping] event was updated payload=" + event);
        return Mono.just(loanRead);
    }
}
