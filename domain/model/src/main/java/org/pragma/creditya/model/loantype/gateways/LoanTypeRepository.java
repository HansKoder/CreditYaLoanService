package org.pragma.creditya.model.loantype.gateways;

import org.pragma.creditya.model.loantype.LoanType;
import org.pragma.creditya.model.loantype.valueobject.LoanTypeId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LoanTypeRepository {
    Mono<Boolean> existLoanType (LoanTypeId loanTypeId);
    Mono<LoanType> findById (Long id);
}
