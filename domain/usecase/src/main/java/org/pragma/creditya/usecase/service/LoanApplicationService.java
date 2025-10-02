package org.pragma.creditya.usecase.service;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.usecase.command.CreateApplicationLoanCommand;
import org.pragma.creditya.usecase.command.handler.customer.ICustomerUseCase;
import org.pragma.creditya.usecase.command.handler.loan.ILoanUseCase;
import org.pragma.creditya.usecase.command.handler.loantype.ILoanTypeUseCase;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LoanApplicationService implements ILoanApplicationService {

    private final ILoanUseCase loanUseCase;
    private final ICustomerUseCase customerUseCase;
    private final ILoanTypeUseCase loanTypeUseCase;

    @Override
    public Mono<Loan> createApplicationSubmitLoan(CreateApplicationLoanCommand command) {
        return loanUseCase.checkApplication(command)
                .flatMap(domain -> customerUseCase
                        .verifyMyIdentity(domain.getDocument())
                        .then(Mono.just(domain))
                ).flatMap(domain -> loanTypeUseCase
                        .getLoanTypeById(domain.getLoanTypeId())
                        .map(loanType -> {
                            domain.markAsSubmitted(loanType.getResolutionType());
                            return domain;
                        })
                        .then(Mono.just(domain))
                );
    }

}
