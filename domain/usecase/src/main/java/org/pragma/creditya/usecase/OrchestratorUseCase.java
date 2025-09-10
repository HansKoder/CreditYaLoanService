package org.pragma.creditya.usecase;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loan.bus.EventBus;
import org.pragma.creditya.model.loan.event.LoanEvent;
import org.pragma.creditya.model.loan.gateways.EventStoreRepository;
import org.pragma.creditya.model.loanread.LoanRead;
import org.pragma.creditya.usecase.command.CreateRequestLoanCommand;
import org.pragma.creditya.usecase.loan.ILoanUseCase;
import org.pragma.creditya.usecase.loanread.ILoanReadUseCase;
import org.pragma.creditya.usecase.loantype.ILoanTypeUseCase;
import org.pragma.creditya.usecase.query.GetLoanQuery;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class OrchestratorUseCase implements IOrchestratorUseCase{

    private final ILoanTypeUseCase loanTypeUseCase;
    private final ILoanUseCase loanUseCase;
    private final EventStoreRepository eventRepository;
    private final ILoanReadUseCase loanReadUseCase;
    private final EventBus eventBus;

    @Override
    public Mono<Loan> applicationLoan(CreateRequestLoanCommand command) {
        return loanUseCase.checkApplication(command)
                .flatMap(loanTypeUseCase::checkLoanTypeExists)
                .flatMap(loan -> {
                    List<LoanEvent> events = loan.getUncommittedEvents();
                    return eventRepository.saveAll(events)
                            .doOnSuccess(v -> events.forEach(eventBus::publish))
                            .then(Mono.just(loan))
                            .doOnSuccess(Loan::clearUncommittedEvents);
                });
    }

    @Override
    public Flux<LoanRead> getLoan(GetLoanQuery query) {
        return loanReadUseCase.getLoan(query);
    }
}
