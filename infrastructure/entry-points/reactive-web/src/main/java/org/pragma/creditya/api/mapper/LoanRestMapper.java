package org.pragma.creditya.api.mapper;

import org.pragma.creditya.api.dto.request.CreateApplicationLoanRequest;
import org.pragma.creditya.api.dto.request.ResolutionApplicationLoanRequest;
import org.pragma.creditya.api.dto.response.LoanApplicationResponse;
import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.usecase.command.CreateApplicationLoanCommand;
import org.pragma.creditya.usecase.command.ResolveApplicationLoanCommand;

public class LoanRestMapper {

    public static CreateApplicationLoanCommand toCommand (CreateApplicationLoanRequest request) {
        return new CreateApplicationLoanCommand(
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
                entity.getDocument().getValue(),
                entity.getAmount().amount(),
                entity.getLoanTypeId().getValue(),
                entity.getPeriod().year(),
                entity.getPeriod().month(),
                entity.getLoanStatus().name()
        );
    }

    public static ResolveApplicationLoanCommand toCommand (ResolutionApplicationLoanRequest request) {
        return new ResolveApplicationLoanCommand(
                request.loanId(),
                request.decision(),
                request.reason(),
                request.resolutionType()
        );
    }
}
