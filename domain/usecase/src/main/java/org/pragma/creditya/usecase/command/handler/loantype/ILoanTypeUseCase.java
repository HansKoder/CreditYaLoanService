package org.pragma.creditya.usecase.command.handler.loantype;

import org.pragma.creditya.model.loan.Loan;
import reactor.core.publisher.Mono;

public interface ILoanTypeUseCase {
    Mono<Loan> checkLoanTypeExists (Loan loan);
}
