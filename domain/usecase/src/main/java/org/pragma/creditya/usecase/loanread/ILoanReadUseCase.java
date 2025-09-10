package org.pragma.creditya.usecase.loanread;

import org.pragma.creditya.model.loanread.LoanRead;
import org.pragma.creditya.usecase.query.GetLoanQuery;
import reactor.core.publisher.Flux;

public interface ILoanReadUseCase {
    Flux<LoanRead> getLoan (GetLoanQuery query);
}
