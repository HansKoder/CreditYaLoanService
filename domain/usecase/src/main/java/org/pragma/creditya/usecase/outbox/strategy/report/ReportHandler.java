package org.pragma.creditya.usecase.outbox.strategy.report;

import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loan.event.LoanEvent;
import org.pragma.creditya.model.loan.event.payload.ApplicationApprovedEvent;
import org.pragma.creditya.model.loan.event.payload.LoanEventPayload;
import org.pragma.creditya.usecase.outbox.payload.OutboxPayload;
import org.pragma.creditya.usecase.outbox.strategy.OutboxStrategy;
import reactor.core.publisher.Mono;

public class ReportHandler implements OutboxStrategy {
    @Override
    public boolean apply(LoanEventPayload event) {
        System.out.println("[use_case.outbox.strategy.report] (apply) payload=[ event:{" + event + "}]");
        return event instanceof ApplicationApprovedEvent;
    }

    @Override
    public Mono<OutboxPayload> handler(Loan domain, LoanEvent event) {
        return Mono.just(ReportMapper.toPayload(domain));
    }
}
