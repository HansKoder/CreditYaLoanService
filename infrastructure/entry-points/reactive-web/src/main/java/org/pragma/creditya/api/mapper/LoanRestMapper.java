package org.pragma.creditya.api.mapper;

import org.pragma.creditya.api.dto.request.CreateApplicationLoanRequest;
import org.pragma.creditya.api.dto.response.LoanAppliedResponse;
import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.usecase.command.CreateRequestLoanCommand;

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
        String loanId = entity.getId().getValue() != null ? entity.getId().getValue().toString() : "";
        return new LoanAppliedResponse(
                loanId,
                entity.getDocument().value(),
                entity.getAmount().amount(),
                entity.getLoanType().code(),
                entity.getPeriod().year(),
                entity.getPeriod().month(),
                entity.getLoanStatus().name()
        );
    }

}
