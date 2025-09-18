package org.pragma.creditya.usecase.loantype;

import org.pragma.creditya.model.loan.Loan;
import reactor.core.publisher.Mono;

public interface ILoanTypeUseCase {
    Mono<Loan> checkLoanTypeExists (Loan loan);
}
