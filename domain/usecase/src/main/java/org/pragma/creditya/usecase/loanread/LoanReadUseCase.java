package org.pragma.creditya.usecase.loanread;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.model.loanread.LoanRead;
import org.pragma.creditya.model.loanread.gateways.LoanReadRepository;
import org.pragma.creditya.usecase.query.GetLoanQuery;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class LoanReadUseCase implements ILoanReadUseCase{

    private final LoanReadRepository loanReadRepository;

    @Override
    public Flux<LoanRead> getLoan (GetLoanQuery query) {
        return loanReadRepository.getLoan();
    }

}
