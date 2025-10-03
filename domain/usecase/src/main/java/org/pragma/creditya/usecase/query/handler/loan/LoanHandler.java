package org.pragma.creditya.usecase.query.handler.loan;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.usecase.query.handler.loan.dto.LoanSummaryDTO;
import org.pragma.creditya.usecase.query.repository.LoanReadRepository;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class LoanHandler implements ILoanHandler {

    private final LoanReadRepository loanReadRepository;

    @Override
    public Flux<LoanSummaryDTO> getLoan (GetLoanQuery query) {
        return loanReadRepository.getLoan(query);
    }

}
