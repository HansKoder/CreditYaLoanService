package org.pragma.creditya.usecase;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loan.bus.EventBus;
import org.pragma.creditya.model.loan.event.LoanApplicationSubmittedEvent;
import org.pragma.creditya.model.loan.event.LoanEvent;
import org.pragma.creditya.model.loan.event.LoanResolutionApprovedEvent;
import org.pragma.creditya.model.loan.event.LoanResolutionRejectedEvent;
import org.pragma.creditya.model.loan.exception.LoanDomainException;
import org.pragma.creditya.model.loan.gateways.EventStoreRepository;
import org.pragma.creditya.model.loan.gateways.OutboxRepository;
import org.pragma.creditya.model.loan.valueobject.LoanId;
import org.pragma.creditya.model.loan.valueobject.LoanStatus;
import org.pragma.creditya.model.loanread.LoanRead;
import org.pragma.creditya.model.loanread.query.LoanQuery;
import org.pragma.creditya.model.loantype.exception.LoanTypeNotFoundDomainException;
import org.pragma.creditya.usecase.command.CreateRequestLoanCommand;
import org.pragma.creditya.usecase.command.DecisionLoanCommand;
import org.pragma.creditya.usecase.loan.ILoanUseCase;
import org.pragma.creditya.usecase.loanread.ILoanReadUseCase;
import org.pragma.creditya.usecase.loantype.ILoanTypeUseCase;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.function.BiFunction;

@RequiredArgsConstructor
public class OrchestratorUseCase implements IOrchestratorUseCase{

    private final ILoanTypeUseCase loanTypeUseCase;
    private final ILoanUseCase loanUseCase;
    private final EventStoreRepository eventRepository;
    private final ILoanReadUseCase loanReadUseCase;
    private final EventBus eventBus;
    private final OutboxRepository outboxRepository;

    @Override
    public Mono<Loan> applicationLoan(CreateRequestLoanCommand command) {
        return loanUseCase.checkApplication(command)
                .flatMap(loanUseCase::verifyOwnershipCustomer)
                .flatMap(loanTypeUseCase::checkLoanTypeExists)
                .flatMap(loanUseCase::markAsPending)
                .flatMap(this::persistAndPublishEvents);
    }

    @Override
    public Flux<LoanRead> getLoans(LoanQuery query) {
        return loanReadUseCase.getLoan(query);
    }

    @Override
    public Mono<Loan> decisionLoan(DecisionLoanCommand command) {
        // Plan B -> outbox events (Builder)
        // Load -> notifyDecisionLoanEvent (outbox)
        // Load -> countApprovedLoan (outbox)
        // Load -> sumAmountApprovedLoan (outbox)

        // check

        return checkTypeDecision(command)
                .flatMap(this::fromStringToUUID)
                .flatMap(this::getLoan)
                .flatMap(loanUseCase::loadUsername)
                .flatMap(loan -> checkDecisionLoan(loan, command))
                .flatMap(this::persistAndPublishEvents)
                .doOnError(e -> System.out.printf("[domain.use_case] (decision loan) payload[ error:%s ] \n", e.getMessage()));
    }

    private Map<String, BiFunction<Loan, String, Mono<Loan>>> buildDecisionHandlers() {
        Map<String, BiFunction<Loan, String, Mono<Loan>>> map = new HashMap<>();
        map.put(LoanStatus.APPROVED.name(), loanUseCase::approvedLoan);
        map.put(LoanStatus.REJECTED.name(), loanUseCase::rejectedLoan);
        return map;
    }

    private Mono<Loan> checkDecisionLoan (Loan loan, DecisionLoanCommand command) {
        BiFunction<Loan, String, Mono<Loan>> handler = buildDecisionHandlers().get(command.decision());
        if (handler == null)
            return Mono.error(new LoanDomainException("Unknown decision: " + command.decision()));

        return handler.apply(loan, command.reason()).log();
    }

    private Mono<DecisionLoanCommand> checkTypeDecision (DecisionLoanCommand command) {
        if (command.decision() == null || command.decision().isBlank())
            return Mono.error(new LoanDomainException("Decision must be mandatory"));

        Set<String> decisionOptions = new HashSet<>();

        decisionOptions.add(LoanStatus.APPROVED.name());
        decisionOptions.add(LoanStatus.REJECTED.name());

        if (!decisionOptions.contains(command.decision()))
            return Mono.error(new LoanDomainException("Unknown decision type"));

        return Mono.just(command);
    }

    private Mono<UUID> fromStringToUUID (DecisionLoanCommand command) {
        if (command.loanId() == null || command.loanId().isBlank())
            return Mono.error(new LoanDomainException("Loan Id must be provided"));

        try {
            UUID aggregateId = UUID.fromString(command.loanId());
            return Mono.just(aggregateId).log();
        } catch (IllegalArgumentException ex) {
            return Mono.error(new LoanDomainException("Invalid loanId format"));
        }
    }

    private Mono<Loan> getLoan (UUID aggregateId) {
        return eventRepository.findByAggregateId(aggregateId)
                .collectList()
                .flatMap(loanUseCase::rehydrate)
                .log();
    }

    private Mono<Loan> persistAndPublishEvents (Loan loan) {
        List<LoanEvent> events = loan.getUncommittedEvents();

        if (events.isEmpty())
            return Mono.just(loan);

        return eventRepository.saveAll(events)
                .doOnSuccess(v -> events.forEach(eventBus::publish))
                .thenReturn(loan)
                .doOnSuccess(Loan::clearUncommittedEvents);
    }

}
