package org.pragma.creditya.outbox.handler;

import org.pragma.creditya.model.loan.Loan;
import reactor.core.publisher.Mono;

public interface IOutboxHandler {

    Mono<Void> execute (Loan loanDomain);

}
