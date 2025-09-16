package org.pragma.creditya.usecase.loan;

import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loan.event.LoanEvent;
import org.pragma.creditya.usecase.command.CreateRequestLoanCommand;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface ILoanUseCase {
    Mono<Loan> checkApplication(CreateRequestLoanCommand cmd);
    Mono<Loan> verifyOwnershipCustomer (Loan loan);
    Mono<Loan> markAsPending (Loan loan);
    Mono<Loan> rehydrate (List<LoanEvent> events);
    Mono<Loan> loadUsername (Loan loan);
    Mono<Loan> approvedLoan (Loan loan, String reason);
    Mono<Loan> rejectedLoan (Loan loan, String reason);
}
