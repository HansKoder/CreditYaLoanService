package org.pragma.creditya.usecase;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loan.event.LoanEvent;
import org.pragma.creditya.model.loan.gateways.EventStoreRepository;
import org.pragma.creditya.usecase.command.CreateRequestLoanCommand;
import org.pragma.creditya.usecase.loan.ILoanUseCase;
import org.pragma.creditya.usecase.loantype.ILoanTypeUseCase;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class OrchestratorUseCase implements IOrchestratorUseCase{

    private final ILoanTypeUseCase loanTypeUseCase;
    private final ILoanUseCase loanUseCase;
    private final EventStoreRepository eventRepository;

    @Override
    public Mono<Loan> applicationLoan(CreateRequestLoanCommand command) {
        return loanUseCase.checkApplication(command)
                .flatMap(loanTypeUseCase::checkLoanTypeExists)
                .flatMap(loan -> {
                    List<LoanEvent> events = loan.getUncommittedEvents();
                    return eventRepository.saveAll(events)
                            .then(Mono.just(loan))
                            .doOnSuccess(Loan::getUncommittedEvents);
                });
    }
}
