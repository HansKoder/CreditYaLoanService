package org.pragma.creditya.api.mapper;

import org.pragma.creditya.api.dto.request.CreateApplicationLoanRequest;
import org.pragma.creditya.api.dto.response.LoanAppliedResponse;
import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.usecase.loan.command.CreateRequestLoanCommand;

public class LoanRestMapper {

    public static CreateRequestLoanCommand toCommand (CreateApplicationLoanRequest request) {
        return new CreateRequestLoanCommand(
                request.document(),
                request.amount(),
                request.loanTypeId(),
                request.year(),
                request.month()
        );
    }

    public static LoanAppliedResponse toResponse (Loan entity) {
        return new LoanAppliedResponse(
                entity.getId().getValue().toString(),
                entity.getDocument().value(),
                entity.getAmount().amount(),
                entity.getLoanType().code(),
                entity.getPeriod().year(),
                entity.getPeriod().month(),
                entity.getLoanStatus().name()
        );
    }

}
