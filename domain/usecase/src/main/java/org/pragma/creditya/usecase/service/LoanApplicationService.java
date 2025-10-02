package org.pragma.creditya.usecase.service;

import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.usecase.command.CreateApplicationLoanCommand;
import reactor.core.publisher.Mono;

public class LoanApplicationService implements ILoanApplicationService {

    @Override
    public Mono<Loan> createApplicationSubmitLoan(CreateApplicationLoanCommand command) {
        return null;
    }

}
