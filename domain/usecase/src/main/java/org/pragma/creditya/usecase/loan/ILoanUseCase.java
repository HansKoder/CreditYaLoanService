package org.pragma.creditya.usecase.loan;

import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.usecase.command.CreateRequestLoanCommand;
import reactor.core.publisher.Mono;

public interface ILoanUseCase {
    Mono<Loan> checkApplication(CreateRequestLoanCommand cmd);
    Mono<Loan> verifyOwnershipCustomer (Loan loan);
    Mono<Loan> markAsPending (Loan loan);
}
