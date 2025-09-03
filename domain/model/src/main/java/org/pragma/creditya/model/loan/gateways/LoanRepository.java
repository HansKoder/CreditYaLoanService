package org.pragma.creditya.model.loan.gateways;

import org.pragma.creditya.model.loan.Loan;
import reactor.core.publisher.Mono;

public interface LoanRepository {
    Mono<Loan> save(Loan entity);
}
