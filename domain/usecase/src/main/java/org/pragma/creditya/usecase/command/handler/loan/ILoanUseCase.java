package org.pragma.creditya.usecase.command.handler.loan;

import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loan.valueobject.LoanStatus;
import org.pragma.creditya.model.loan.valueobject.Resolution;
import org.pragma.creditya.usecase.command.CreateApplicationLoanCommand;
import reactor.core.publisher.Mono;

public interface ILoanUseCase {
    Mono<Loan> checkApplication(CreateApplicationLoanCommand command);
    Mono<Loan> getLoan(String loanId);
    Mono<LoanStatus> checkDecisionType(String decision);
    Mono<Loan> resolutionApplicationLoan(Loan domain, Resolution resolution);
    Mono<Loan> persist (Loan domain);
}
