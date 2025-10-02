package org.pragma.creditya.usecase.command.handler.loan;

import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loan.event.LoanEvent;
import org.pragma.creditya.model.loan.valueobject.LoanId;
import org.pragma.creditya.model.loan.valueobject.LoanStatus;
import org.pragma.creditya.usecase.command.CreateApplicationLoanCommand;
import reactor.core.publisher.Mono;

import java.util.List;

/*
public interface ILoanUseCase {
    Mono<Loan> checkApplication(CreateApplicationLoanCommand cmd);
    Mono<Loan> verifyOwnershipCustomer (Loan loan);
    Mono<Loan> markAsPending (Loan loan);
    Mono<Loan> rehydrate (List<LoanEvent> events);
    Mono<Loan> loadUsername (Loan loan);
    Mono<Loan> approvedLoan (Loan loan, String reason);
    Mono<Loan> rejectedLoan (Loan loan, String reason);
}

 */


public interface ILoanUseCase {
    Mono<Loan> checkApplication(CreateApplicationLoanCommand command);
    Mono<Loan> getLoan(String loanId);
    Mono<LoanStatus> checkDecisionType(String decision);
    Mono<Loan> resolutionApplicationLoan(Loan domain);
    Mono<Loan> persist (Loan domain);
}
