package org.pragma.creditya.usecase.service;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loan.valueobject.LoanStatus;
import org.pragma.creditya.model.loan.valueobject.Resolution;
import org.pragma.creditya.model.loantype.valueobject.ResolutionType;
import org.pragma.creditya.usecase.command.CreateApplicationLoanCommand;
import org.pragma.creditya.usecase.command.ResolveApplicationLoanCommand;
import org.pragma.creditya.usecase.command.handler.customer.ICustomerUseCase;
import org.pragma.creditya.usecase.command.handler.loan.ILoanUseCase;
import org.pragma.creditya.usecase.command.handler.loantype.ILoanTypeUseCase;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.Instant;

@RequiredArgsConstructor
public class LoanApplicationService implements ILoanApplicationService {

    private final ILoanUseCase loanUseCase;
    private final ICustomerUseCase customerUseCase;
    private final ILoanTypeUseCase loanTypeUseCase;
    private final ServiceMapper serviceMapper;

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
                ).flatMap(loanUseCase::persist);
    }

    @Override
    public Mono<Loan> resolutionApplicationLoan(ResolveApplicationLoanCommand command) {
        return loanUseCase.getLoan(command.loanId())
                .zipWith(checkResolution(command))
                .flatMap(tuple2 -> loanUseCase.resolutionApplicationLoan(tuple2.getT1(), tuple2.getT2()))
                .flatMap(loanUseCase::persist);
    }

    private Mono<Resolution> checkResolution (ResolveApplicationLoanCommand command) {
        return loanUseCase.checkDecisionType(command.decision())
                .zipWith(loanTypeUseCase.checkResolutionType(command.resolutionType()))
                .flatMap(t -> buildResolution(t, command.reason()));
    }

    private Mono<Resolution> buildResolution (Tuple2<LoanStatus, ResolutionType> tuple2, String reason) {
        return serviceMapper.getDecidedBy(tuple2.getT2())
                .map(username -> new Resolution(
                   username,
                   reason,
                   tuple2.getT1()
                ));
    }


}
