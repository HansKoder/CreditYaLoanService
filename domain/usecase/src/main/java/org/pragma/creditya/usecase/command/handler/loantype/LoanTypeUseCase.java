package org.pragma.creditya.usecase.command.handler.loantype;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loantype.LoanType;
import org.pragma.creditya.model.loantype.exception.LoanTypeNotFoundDomainException;
import org.pragma.creditya.model.loantype.gateways.LoanTypeRepository;
import org.pragma.creditya.model.loantype.valueobject.LoanTypeId;
import org.pragma.creditya.model.loantype.valueobject.ResolutionType;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LoanTypeUseCase implements ILoanTypeUseCase {

    private final LoanTypeRepository loanTypeRepository;
    private final LoanTypeHelper loanTypeHelper;

    @Override
    public Mono<LoanType> getLoanTypeById(LoanTypeId id) {
        return loanTypeRepository.findById(id)
                .switchIfEmpty(Mono.error(new LoanTypeNotFoundDomainException("Loan Type with the ID " + id + " is not found")));
    }

    @Override
    public Mono<ResolutionType> checkResolutionType(String resolution) {
        return loanTypeHelper.checkResolutionType(resolution);
    }

}
