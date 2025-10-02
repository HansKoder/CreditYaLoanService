package org.pragma.creditya.usecase.service;

import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.usecase.command.CreateApplicationLoanCommand;
import org.pragma.creditya.usecase.command.ResolveApplicationLoanCommand;
import reactor.core.publisher.Mono;

public interface ILoanApplicationService {

    Mono<Loan> createApplicationSubmitLoan (CreateApplicationLoanCommand command);
    Mono<Loan> resolutionApplicationLoan (ResolveApplicationLoanCommand command);

}
