package org.pragma.creditya.usecase.outbox.strategy;

import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loan.event.LoanEvent;
import org.pragma.creditya.model.loan.event.LoanEventPayload;
import org.pragma.creditya.usecase.outbox.payload.OutboxPayload;
import reactor.core.publisher.Mono;

public interface OutboxStrategy {
    boolean apply(LoanEventPayload event);
    Mono<OutboxPayload> handler(Loan domain, LoanEvent event);
}
