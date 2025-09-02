package org.pragma.creditya.r2dbc.persistence.loan.mapper;

import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loan.valueobject.LoanStatus;
import org.pragma.creditya.r2dbc.helper.CustomMapper;
import org.pragma.creditya.r2dbc.persistence.loan.entity.LoanEntity;
import org.pragma.creditya.r2dbc.persistence.loan.entity.LoanStatusEntity;
import org.springframework.stereotype.Component;

@Component
public class LoanMapper implements CustomMapper<Loan, LoanEntity> {
    @Override
    public LoanEntity toData(Loan entity) {
        return LoanEntity
                .builder()
                .loanId(entity.getId().getValue())
                .status(LoanStatusEntity.valueOf(entity.getLoanStatus().name()))
                .document(entity.getDocument().value())
                .amount(entity.getAmount().amount())
                .year(entity.getPeriod().year())
                .month(entity.getPeriod().month())
                .loanTypeId(entity.getLoanType().code())
                .build();
    }

    @Override
    public Loan toEntity(LoanEntity data) {
        return Loan.LoanBuilder
                .aLoan()
                .id(data.getLoanId())
                .document(data.getDocument())
                .loanStatus(LoanStatus.valueOf(data.getStatus().name()))
                .period(data.getYear(), data.getMonth())
                .amount(data.getAmount())
                .loanType(data.getLoanTypeId())
                .build();

    }
}
