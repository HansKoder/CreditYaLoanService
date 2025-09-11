package org.pragma.creditya.mongo.mapper;

import org.pragma.creditya.model.loanread.LoanRead;
import org.pragma.creditya.mongo.collection.LoanReadCollection;
import org.springframework.stereotype.Component;

@Component
public class LoanReadCustomMapper implements CustomMapper<LoanRead, LoanReadCollection> {

    @Override
    public LoanReadCollection toData(LoanRead entity) {
        return LoanReadCollection.builder()
                .id(entity.getId())
                .loanId(entity.getLoanId())
                .amount(entity.getAmount())
                .document(entity.getDocument())
                .totalMonthlyDebt(entity.getTotalMonthlyDebt())
                .months(entity.getMonths())
                .typeLoan(entity.getTypeLoan())
                .status(entity.getStatus())
                .timestamp(entity.getTimestamp())
                .build();
    }

    @Override
    public LoanRead toEntity(LoanReadCollection data) {
        return LoanRead
                .builder()
                .id(data.getId())
                .loanId(data.getLoanId())
                .document(data.getDocument())
                .amount(data.getAmount())
                .status(data.getStatus())
                .months(data.getMonths())
                .timestamp(data.getTimestamp())
                .typeLoan(data.getTypeLoan())
                .totalMonthlyDebt(data.getTotalMonthlyDebt())
                .build();
    }
}
