package org.pragma.creditya.r2dbc.persistence.loan.mapper;

import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loan.valueobject.LoanStatus;
import org.pragma.creditya.r2dbc.helper.CustomMapper;
import org.pragma.creditya.r2dbc.persistence.loan.entity.LoanEntity;
import org.pragma.creditya.r2dbc.persistence.loan.entity.LoanStatusEntity;

public class LoanMapper implements CustomMapper<Loan, LoanEntity> {
    @Override
    public LoanEntity toData(Loan entity) {
        LoanEntity data = new LoanEntity();

        data.setDocument(entity.getDocument().value());
        data.setLoanId(entity.getId().getValue());
        data.setStatus(LoanStatusEntity.valueOf(entity.getLoanStatus().name()));
        data.setAmount(entity.getAmount().amount());


        return data;
    }

    @Override
    public Loan toEntity(LoanEntity data) {
        return Loan.LoanBuilder
                .aLoan()
                .id(data.getLoanId())
                .document(data.getDocument())
                .loanStatus(LoanStatus.valueOf(data.getStatus().name()))
                .period(data.getYear(), data.getMonth())
                .build();

    }
}
