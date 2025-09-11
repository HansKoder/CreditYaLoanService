package org.pragma.creditya.r2dbc.persistence.loantype.mapper;

import org.pragma.creditya.model.loantype.LoanType;
import org.pragma.creditya.r2dbc.helper.CustomMapper;
import org.pragma.creditya.r2dbc.persistence.loantype.entity.LoanTypeEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LoanTypeMapper implements CustomMapper<LoanType, LoanTypeEntity> {

    private final Logger logger = LoggerFactory.getLogger(LoanTypeMapper.class);

    @Override
    public LoanTypeEntity toData(LoanType entity) {
        logger.info("[infra.r2dbc] (mapper) (toData) payload=[ entity:{} ]", entity);

        return LoanTypeEntity.builder()
                .id(entity.getId().getValue())
                .description(entity.getDescription().value())
                .interestRate(entity.getInterestRate().value())
                .build();
    }

    @Override
    public LoanType toEntity(LoanTypeEntity data) {
        logger.info("[infra.r2dbc] (mapper) (toEntity) payload=[ data:{} ]", data);

        return LoanType.LoanTypeBuilder.aLoanType()
                .id(data.getId())
                .description(data.getDescription())
                .interestRate(data.getInterestRate())
                .build();
    }

}
