package org.pragma.creditya.usecase.loan;

import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.usecase.loan.command.CreateRequestLoanCommand;
import reactor.core.publisher.Mono;

public interface ILoanUseCase {
    Mono<Loan> createRequestLoan (CreateRequestLoanCommand cmd);
}
