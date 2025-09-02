package org.pragma.creditya.usecase;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.usecase.command.CreateRequestLoanCommand;
import org.pragma.creditya.usecase.loan.ILoanUseCase;
import org.pragma.creditya.usecase.loantype.ILoanTypeUseCase;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class OrchestratorUseCase implements IOrchestratorUseCase{

    private final ILoanTypeUseCase loanTypeUseCase;
    private final ILoanUseCase loanUseCase;

    @Override
    public Mono<Loan> applicationLoan(CreateRequestLoanCommand command) {
        return loanUseCase.checkApplication(command)
                .flatMap(loanTypeUseCase::checkLoanTypeExists)
                .flatMap(loanUseCase::persist);

    }
}
