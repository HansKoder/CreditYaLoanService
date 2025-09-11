package org.pragma.creditya.api.mapper;

import org.pragma.creditya.api.dto.request.CreateApplicationLoanRequest;
import org.pragma.creditya.api.dto.response.LoanApplicationResponse;
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

    public static LoanApplicationResponse toResponse (Loan entity) {
        String loanId = entity.getId().getValue() != null ? entity.getId().getValue().toString() : "";
        return new LoanApplicationResponse(
                loanId,
                entity.getDocument().value(),
                entity.getAmount().amount(),
                entity.getLoanTypeCode().code(),
                entity.getPeriod().year(),
                entity.getPeriod().month(),
                entity.getLoanStatus().name()
        );
    }

}
