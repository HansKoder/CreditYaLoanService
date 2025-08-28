package org.pragma.creditya.r2dbc.persistence.adapter;

import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loan.gateways.LoanRepository;
import org.pragma.creditya.r2dbc.helper.ReactiveAdapterOperations;
import org.pragma.creditya.r2dbc.persistence.entity.LoanEntity;
import org.pragma.creditya.r2dbc.persistence.mapper.LoanMapper;
import org.pragma.creditya.r2dbc.persistence.repository.LoanReactiveRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class LoanRepositoryAdapter extends ReactiveAdapterOperations<
        Loan,
        LoanEntity,
        UUID,
        LoanReactiveRepository
        > implements LoanRepository {
    public LoanRepositoryAdapter(LoanReactiveRepository repository, LoanMapper mapper) {
        super(repository, mapper, mapper::toEntity);
    }


}
