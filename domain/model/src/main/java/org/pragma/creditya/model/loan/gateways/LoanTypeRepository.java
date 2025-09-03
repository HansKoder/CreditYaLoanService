package org.pragma.creditya.model.loan.gateways;

import reactor.core.publisher.Mono;

public interface LoanTypeRepository {
    Mono<Boolean> existLoanType (Long loanTypeId);
}
