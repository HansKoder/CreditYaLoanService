package org.pragma.creditya.model.loantype.gateways;

import org.pragma.creditya.model.loantype.valueobject.LoanTypeId;
import reactor.core.publisher.Mono;

public interface LoanTypeRepository {
    Mono<Boolean> existLoanType (LoanTypeId loanTypeId);
}
