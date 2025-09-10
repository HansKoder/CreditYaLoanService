package org.pragma.creditya.usecase.loanread;

import org.pragma.creditya.model.loanread.LoanRead;
import org.pragma.creditya.model.loanread.query.LoanQuery;
import reactor.core.publisher.Flux;

public interface ILoanReadUseCase {
    Flux<LoanRead> getLoan (LoanQuery query);
}
