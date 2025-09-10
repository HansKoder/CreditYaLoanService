package org.pragma.creditya.r2dbc.persistence.loantype.mapper;

import org.pragma.creditya.model.loantype.LoanType;
import org.pragma.creditya.r2dbc.helper.CustomMapper;
import org.pragma.creditya.r2dbc.persistence.loantype.entity.LoanTypeEntity;
import org.springframework.stereotype.Component;

@Component
public class LoanTypeMapper implements CustomMapper<LoanType, LoanTypeEntity> {

    @Override
    public LoanTypeEntity toData(LoanType entity) {
        return null;
    }

    @Override
    public LoanType toEntity(LoanTypeEntity data) {
        return null;
    }

}
