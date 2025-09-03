package org.pragma.creditya.r2dbc.persistence.type.adapter;

import org.pragma.creditya.model.loan.gateways.LoanTypeRepository;
import org.pragma.creditya.model.loantype.LoanType;
import org.pragma.creditya.r2dbc.helper.ReactiveAdapterOperations;
import org.pragma.creditya.r2dbc.persistence.type.entity.LoanTypeEntity;
import org.pragma.creditya.r2dbc.persistence.type.mapper.LoanTypeMapper;
import org.pragma.creditya.r2dbc.persistence.type.repository.LoanTypeReactiveRepository;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class LoanTypeRepositoryAdapter extends ReactiveAdapterOperations<
        LoanType,
        LoanTypeEntity,
        Long,
        LoanTypeReactiveRepository
        > implements LoanTypeRepository {


    public LoanTypeRepositoryAdapter(LoanTypeReactiveRepository repository, LoanTypeMapper mapper) {
        super(repository, mapper, mapper::toEntity);
    }

    @Override
    public Mono<Boolean> existLoanType(Long loanTypeId) {
        LoanTypeEntity data = LoanTypeEntity.builder()
                .id(loanTypeId)
                .build();

        return repository.exists(Example.of(data));
    }
}
