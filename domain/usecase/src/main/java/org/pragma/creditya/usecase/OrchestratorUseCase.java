package org.pragma.creditya.usecase;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loan.bus.EventBus;
import org.pragma.creditya.model.loan.event.LoanEvent;
import org.pragma.creditya.model.loan.gateways.EventStoreRepository;
import org.pragma.creditya.model.loanread.LoanRead;
import org.pragma.creditya.model.loanread.query.LoanQuery;
import org.pragma.creditya.usecase.command.CreateRequestLoanCommand;
import org.pragma.creditya.usecase.loan.ILoanUseCase;
import org.pragma.creditya.usecase.loanread.ILoanReadUseCase;
import org.pragma.creditya.usecase.loantype.ILoanTypeUseCase;
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
                .flatMap(loanUseCase::verifyOwnershipCustomer)
                .flatMap(loanTypeUseCase::checkLoanTypeAndLoad)
                .flatMap(loanUseCase::markAsPending)
                .flatMap(this::persistAndPublishEvents);
    }

    // resolutionLoan
    // command -> ResolutionLoanCommand ( loanId: string (uuid), type: string enum ['APPROVED', 'REJECTED'], reason string (optional)
    // check type of resolution (param) -> method private - check type-resolution -> this is null or other loanDomException type resolution is unknown
    //                             using loanStatus -> must be equals.
    // check loanId (param) -> null -> exception.
    // get Loan using eventRepository -> getByAggregateId (rehydrate)
    // getAuthor -> using jwt claims get username - responsible by
    // check resolution (approved - rejected)
    // persist and publish events (event storing)
    // get customer by document - extract fullName - email
    // outbox pattern


    @Override
    public Flux<LoanRead> getLoans(LoanQuery query) {
        return loanReadUseCase.getLoan(query);
    }

    private Mono<Loan> persistAndPublishEvents (Loan loan) {
        List<LoanEvent> events = loan.getUncommittedEvents();

        if (events.isEmpty()) return Mono.just(loan);

        return eventRepository.saveAll(events)
                .doOnSuccess(v -> events.forEach(eventBus::publish))
                .thenReturn(loan)
                .doOnSuccess(Loan::clearUncommittedEvents);
    }


}
