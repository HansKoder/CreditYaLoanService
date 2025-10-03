package org.pragma.creditya.usecase;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loan.bus.EventBus;
import org.pragma.creditya.model.loan.gateways.EventStoreRepository;
import org.pragma.creditya.usecase.query.handler.loan.dto.LoanSummaryDTO;
import org.pragma.creditya.usecase.query.handler.loan.GetLoanQuery;
import org.pragma.creditya.usecase.outbox.handler.IOutboxHandler;
import org.pragma.creditya.usecase.command.CreateApplicationLoanCommand;
import org.pragma.creditya.usecase.command.ResolveApplicationLoanCommand;
import org.pragma.creditya.usecase.command.handler.loan.ILoanUseCase;
import org.pragma.creditya.usecase.query.handler.loan.ILoanHandler;
import org.pragma.creditya.usecase.command.handler.loantype.ILoanTypeUseCase;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class OrchestratorUseCase implements IOrchestratorUseCase{

    private final ILoanTypeUseCase loanTypeUseCase;
    private final ILoanUseCase loanUseCase;
    private final EventStoreRepository eventRepository;
    private final ILoanHandler loanReadUseCase;
    private final EventBus eventBus;
    private final IOutboxHandler outboxProcess;

    @Override
    public Mono<Loan> applicationLoan(CreateApplicationLoanCommand command) {
        return Mono.empty();
    }

    @Override
    public Flux<LoanSummaryDTO> getLoans(GetLoanQuery query) {
        return Flux.empty();
    }

    @Override
    public Mono<Loan> decisionLoan(ResolveApplicationLoanCommand command) {
        return Mono.empty();
    }

    @Override
    public Mono<Loan> outboxProcess(Loan domain) {
        System.out.println("[domain.use_case] (outboxProcess) payload=domain:" + domain);
        return outboxProcess.execute(domain)
                .then(Mono.just(domain));
    }


}
