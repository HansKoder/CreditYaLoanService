package org.pragma.creditya.usecase.service;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.model.customer.valueobject.Document;
import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loantype.valueobject.LoanTypeId;
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
                .flatMap(loan -> customerUseCase
                        .verifyMyIdentity(loan.getDocument())
                        .then(Mono.just(loan))
                ).flatMap(loan -> loanTypeUseCase
                        .getLoanTypeById(loan.getLoanTypeId())
                        .map(loanType -> {

                        })
                );
    }

}
