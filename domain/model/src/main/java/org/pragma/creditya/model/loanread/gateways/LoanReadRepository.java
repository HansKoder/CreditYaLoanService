package org.pragma.creditya.model.loanread.gateways;

import org.pragma.creditya.model.loanread.LoanRead;
import org.pragma.creditya.model.loanread.query.LoanQuery;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LoanReadRepository {

    Mono<LoanRead> saveLoanRead (LoanRead read);
    Flux<LoanRead> getLoan (LoanQuery query);

}
