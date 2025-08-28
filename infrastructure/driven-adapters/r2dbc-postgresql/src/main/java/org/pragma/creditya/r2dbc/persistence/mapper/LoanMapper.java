package org.pragma.creditya.r2dbc.persistence.mapper;

import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loan.valueobject.LoanStatus;
import org.pragma.creditya.r2dbc.helper.CustomMapper;
import org.pragma.creditya.r2dbc.persistence.entity.LoanEntity;
import org.pragma.creditya.r2dbc.persistence.entity.LoanStatusEntity;

public class LoanMapper implements CustomMapper<Loan, LoanEntity> {
    @Override
    public LoanEntity toData(Loan entity) {
        LoanEntity data = new LoanEntity();

        data.setDocument(entity.getDocument().value());
        data.setLoanId(entity.getId().getValue());
        data.setStatus(LoanStatusEntity.valueOf(entity.getLoanStatus().name()));
        data.setAmount(entity.getAmount().amount());
        data.setPeriod(entity.getPeriod().period());

        return data;
    }

    @Override
    public Loan toEntity(LoanEntity data) {
        return Loan.rebuild(
                data.getLoanId(),
                data.getDocument(),
                data.getAmount(),
                data.getPeriod(),
                LoanStatus.valueOf(data.getStatus().name())
        );
    }
}
