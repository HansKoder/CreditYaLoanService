package org.pragma.creditya.r2dbc.persistence.loantype.adapter;

import org.pragma.creditya.model.loantype.LoanType;
import org.pragma.creditya.model.loantype.gateways.LoanTypeRepository;
import org.pragma.creditya.model.loantype.valueobject.LoanTypeId;
import org.pragma.creditya.r2dbc.helper.ReactiveAdapterOperations;
import org.pragma.creditya.r2dbc.persistence.loantype.entity.LoanTypeEntity;
import org.pragma.creditya.r2dbc.persistence.loantype.mapper.LoanTypeMapper;
import org.pragma.creditya.r2dbc.persistence.loantype.repository.LoanTypeReactiveRepository;
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
    public Mono<Boolean> existLoanType(LoanTypeId loanTypeId) {
        LoanTypeEntity data = LoanTypeEntity.builder()
                .id(loanTypeId.getValue())
                .build();

        return repository.exists(Example.of(data));
    }


}
