package org.pragma.creditya.usecase.service;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.usecase.command.CreateApplicationLoanCommand;
import org.pragma.creditya.usecase.command.ResolveApplicationLoanCommand;
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

    @Override
    public Mono<Loan> resolutionApplicationLoan(ResolveApplicationLoanCommand command) {

        /**
        String loanId,
        String decision,
        String resolutionType,
        String reason
        **/

        // 1) getLoanById() -> loanUseCase -> helper check UUID, rehydrate, get current Loan (LOAN) AR
        // 2) Method private -> checkDecisionApplicationLoan ->
        // 2.1) checkDecisionApplicationLoan() -> loanUseCase -> helper checkDecisionType LoanStatus (VO)
        // 2.2) checkDecisionApplicationLoan() -> loanTypeUseCae -> helper checkResolutionType, ResolutionType (VO)
        // 3) getDecisionBy -> serviceMapper -> if resolutionType == manual then userInfo getUsername | else automatic (hardcoded)
        // build ResolutionVO{by, reason, decision['A', 'R']} by -> username, automatic
        // 4) buildResolution() -> service -> use 2.1, 2.2, and 3, with that build a new vo (decisionVO{by, reason, decision, reason})
        // 5) resolutionApplicationLoan -> loanUseCase -> domain.checkResolution(vo): void
        // 6) question -> should be applied outbox ? before persisting event sourcing
        // 7) persist -> loanUseCae -> loanHelper -> persist events and clean

        // loanUseCase -> getLoan(LoanId), checkDecisionType(String decision), resolutionApplicationLoan (vo), persist(Loan)


        return null;
    }


}
