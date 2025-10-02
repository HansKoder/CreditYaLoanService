package org.pragma.creditya.usecase.command.handler.loan;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loan.bus.EventBus;
import org.pragma.creditya.usecase.command.CreateApplicationLoanCommand;
import org.pragma.creditya.usecase.outbox.handler.IOutboxHandler;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LoanUseCase implements ILoanUseCase {

    private final EventBus eventBus;
    private final IOutboxHandler outboxProcess;

    @Override
    public Mono<Loan> checkApplication(CreateApplicationLoanCommand cmd) {
        return Mono.fromCallable(() -> checkApplicationLoan(cmd));
    }

    /*
    @Override
    public Mono<Loan> rehydrate(List<LoanEvent> events) {
        return Mono.just(Loan.rehydrate(events));
    }

    @Override
    public Mono<Loan> approvedLoan(Loan loan, String reason) {
        return Mono.fromCallable(() -> {
           loan.checkApprovedLoan(reason);
           return loan;
        });
    }

    @Override
    public Mono<Loan> rejectedLoan(Loan loan, String reason) {
        return Mono.fromCallable(() -> {
            loan.checkRejectedLoan(reason);
            return loan;
        });
    }
    */


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
