package org.pragma.creditya.usecase.command.handler.loan;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loan.bus.EventBus;
import org.pragma.creditya.model.loan.exception.LoanDomainException;
import org.pragma.creditya.model.loan.valueobject.LoanStatus;
import org.pragma.creditya.model.loan.valueobject.Resolution;
import org.pragma.creditya.usecase.command.CreateApplicationLoanCommand;
import org.pragma.creditya.usecase.command.ResolveApplicationLoanCommand;
import org.pragma.creditya.usecase.outbox.handler.IOutboxHandler;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
public class LoanUseCase implements ILoanUseCase {

    private final LoanHelper loanHelper;

    @Override
    public Mono<Loan> checkApplication(CreateApplicationLoanCommand cmd) {
        return Mono.fromCallable(() -> checkApplicationLoan(cmd));
    }

    @Override
    public Mono<Loan> getLoan(String loanId) {
        return loanHelper.fromStringToUUID(loanId)
                .flatMap(loanHelper::getLoanById);
    }

    @Override
    public Mono<LoanStatus> checkDecisionType(String decision) {
        return loanHelper.checkDecisionType(decision);
    }

    @Override
    public Mono<Loan> resolutionApplicationLoan(Loan domain, Resolution resolution) {
        domain.resolutionApplicationLoan(resolution);
        return Mono.just(domain);
    }

    @Override
    public Mono<Loan> persist(Loan domain) {
        return loanHelper.persistAndPublishEvents(domain);
    }

    private Loan checkApplicationLoan (CreateApplicationLoanCommand cmd) {
        Loan domain = Loan.LoanBuilder.aLoan()
                .document(cmd.document())
                .loanTypeId(cmd.loanTypeId())
                .amount(cmd.amount())
                .period(cmd.year(), cmd.month())
                .build();

        domain.checkApplicationLoan();

        return domain;
    }

}
