package org.pragma.creditya.usecase;

import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.usecase.command.CreateRequestLoanCommand;
import reactor.core.publisher.Mono;

public interface IOrchestratorUseCase {

    Mono<Loan> applicationLoan (CreateRequestLoanCommand command);

}
