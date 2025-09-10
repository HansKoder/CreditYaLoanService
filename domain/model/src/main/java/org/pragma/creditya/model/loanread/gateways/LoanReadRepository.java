package org.pragma.creditya.model.loanread.gateways;

import org.pragma.creditya.model.loanread.LoanRead;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LoanReadRepository {

    Mono<Void> saveLoanRead (LoanRead read);

    Flux<LoanRead> getLoan ();

}
