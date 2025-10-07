package org.pragma.creditya.usecase.query.handler.loan;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.usecase.query.handler.loan.dto.LoanSummaryDTO;
import org.pragma.creditya.usecase.query.repository.LoanReadRepository;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class LoanQuery implements ILoanQuery {

    private final LoanReadRepository loanReadRepository;

    @Override
    public Flux<LoanSummaryDTO> getLoans (GetLoanFilter query) {
        return loanReadRepository.getLoan(query);
    }

}
