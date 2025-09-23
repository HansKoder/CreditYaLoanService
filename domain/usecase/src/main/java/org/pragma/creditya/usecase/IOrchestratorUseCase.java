package org.pragma.creditya.usecase;

import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loanread.LoanRead;
import org.pragma.creditya.model.loanread.query.LoanQuery;
import org.pragma.creditya.usecase.command.CreateRequestLoanCommand;
import org.pragma.creditya.usecase.command.DecisionLoanCommand;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IOrchestratorUseCase {

    Mono<Loan> applicationLoan (CreateRequestLoanCommand command);
    Flux<LoanRead> getLoans (LoanQuery query);
    Mono<Loan> decisionLoan(DecisionLoanCommand command);
}
