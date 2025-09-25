package org.pragma.creditya.outbox.strategy;

import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loan.event.LoanEvent;
import reactor.core.publisher.Mono;

public interface OutboxStrategy {
    boolean apply(LoanEvent event);
    Mono<?> handler(Loan domain, LoanEvent event);
}
