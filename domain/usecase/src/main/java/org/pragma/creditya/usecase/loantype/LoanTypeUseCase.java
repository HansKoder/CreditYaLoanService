package org.pragma.creditya.usecase.loantype;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loantype.exception.LoanTypeNotFoundDomainException;
import org.pragma.creditya.model.loantype.gateways.LoanTypeRepository;
import org.pragma.creditya.model.loantype.valueobject.LoanTypeId;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LoanTypeUseCase implements ILoanTypeUseCase {

    private final LoanTypeRepository loanTypeRepository;

    @Override
    public Mono<Loan> checkLoanTypeExists(Loan loan) {
        return loanTypeRepository.existLoanType(new LoanTypeId(loan.getLoanType().code()))
                .flatMap(exist -> {
                    if (exist)
                        return Mono.just(loan);

                    String err = String.format("Type Loan code %s does not exist, you need to check", loan.getLoanType().code());
                    return Mono.error(new LoanTypeNotFoundDomainException(err));
                });
    }
}
