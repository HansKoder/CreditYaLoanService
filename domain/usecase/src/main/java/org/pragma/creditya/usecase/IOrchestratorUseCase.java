package org.pragma.creditya.usecase;

import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loanread.LoanRead;
import org.pragma.creditya.usecase.command.CreateRequestLoanCommand;
import org.pragma.creditya.usecase.query.GetLoanQuery;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IOrchestratorUseCase {

    Mono<Loan> applicationLoan (CreateRequestLoanCommand command);
    Flux<LoanRead> getLoan (GetLoanQuery query);

}
