package org.pragma.creditya.r2dbc.persistence.type.adapter;

import org.pragma.creditya.model.loan.gateways.LoanTypeRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class LoanTypeRepositoryAdapter implements LoanTypeRepository {
    @Override
    public Mono<Boolean> existLoanType(Long loanTypeId) {
        return Mono.just(Boolean.TRUE);
    }
}
