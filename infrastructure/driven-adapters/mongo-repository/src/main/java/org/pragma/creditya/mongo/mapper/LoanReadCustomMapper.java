package org.pragma.creditya.mongo.mapper;

import org.pragma.creditya.mongo.collection.LoanReadCollection;
import org.pragma.creditya.usecase.query.handler.loan.dto.LoanSummaryDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LoanReadCustomMapper implements CustomMapper<LoanSummaryDTO, LoanReadCollection> {

    private final Logger logger = LoggerFactory.getLogger(LoanReadCustomMapper.class);

    @Override
    public LoanReadCollection toData(LoanSummaryDTO entity) {
        logger.info("[infra.mongo.mapper] (toData) payload: [ entity:{} ]", entity);
        return LoanReadCollection.builder()
                .id(entity.getId())
                .loanId(entity.getLoanId())
                .amount(entity.getAmount())
                .totalMonthlyDebt(entity.getTotalMonthlyDebt())
                .months(entity.getMonths())
                .typeLoan(entity.getTypeLoan())
                .status(entity.getStatus())
                .timestamp(entity.getTimestamp())
                .document(entity.getDocument())
                .build();
    }

    @Override
    public LoanSummaryDTO toEntity(LoanReadCollection data) {
        logger.info("[infra.mongo.mapper] (toEntity) payload: [ data:{} ]", data);
        return LoanSummaryDTO
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
