package org.pragma.creditya.usecase.query;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.usecase.query.handler.loan.GetLoanFilter;
import org.pragma.creditya.usecase.query.handler.loan.ILoanQuery;
import org.pragma.creditya.usecase.query.handler.loan.dto.LoanSummaryDTO;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class Query implements IQuery{

    private final ILoanQuery loanHandler;

    @Override
    public Flux<LoanSummaryDTO> getLoans(GetLoanFilter query) {
        return loanHandler.getLoans(query);
    }
}
