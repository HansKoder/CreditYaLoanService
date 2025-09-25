package org.pragma.creditya.outbox.processor;

import org.pragma.creditya.model.loan.Loan;
import reactor.core.publisher.Mono;

public interface IOutboxHandler {

    Mono<Void> execute (Loan loanDomain);

}
