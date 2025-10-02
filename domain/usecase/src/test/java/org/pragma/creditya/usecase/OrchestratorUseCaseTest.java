package org.pragma.creditya.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loan.bus.EventBus;
import org.pragma.creditya.model.loan.event.*;
import org.pragma.creditya.model.loan.exception.LoanDomainException;
import org.pragma.creditya.model.loan.gateways.EventStoreRepository;
import org.pragma.creditya.model.loan.valueobject.LoanStatus;
import org.pragma.creditya.usecase.outbox.handler.IOutboxHandler;
import org.pragma.creditya.usecase.command.CreateApplicationLoanCommand;
import org.pragma.creditya.usecase.command.ResolveApplicationLoanCommand;
import org.pragma.creditya.usecase.command.handler.loan.ILoanUseCase;
import org.pragma.creditya.usecase.command.handler.loan.LoanUseCase;
import org.pragma.creditya.usecase.query.loan.ILoanReadUseCase;
import org.pragma.creditya.usecase.query.loan.LoanReadUseCase;
import org.pragma.creditya.usecase.command.handler.loantype.ILoanTypeUseCase;
import org.pragma.creditya.usecase.command.handler.loantype.LoanTypeUseCase;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class OrchestratorUseCaseTest {

    @Mock
    private ILoanUseCase loanUseCase;

    @Mock
    private ILoanTypeUseCase loanTypeUseCase;

    @Mock
    private EventStoreRepository eventStoreRepository;

    @Mock
    private ILoanReadUseCase loanReadUseCase;

    @Mock
    private IOutboxHandler outboxHandler;

    @Mock
    private EventBus eventBus;

    @InjectMocks
    private OrchestratorUseCase orchestratorUseCase;

    private final String LOAN_ID_EXAMPLE = "e36702e6-78d0-4368-a191-292f53c8141c";

    private final Loan LOAN_STATUS_PENDING = Loan.LoanBuilder.aLoan()
            .loanStatus(LoanStatus.PENDING)
            .amount(BigDecimal.valueOf(4_000_000))
            .loanTypeCode(1L)
            .period(1,0)
            .document("103")
            .build();

    // Dummy event mock unknowing event
    static class UnknownLoanEvent extends LoanEvent {
        public UnknownLoanEvent() {
            super();
            super.setAggregateId(UUID.randomUUID());
            super.setId(UUID.randomUUID());
            super.setEventType(EventType.LOAN_SUBMITTED);
        }
    }

    private final LoanApplicationSubmittedEvent SUBMITTED_EVENT =
            LoanApplicationSubmittedEvent.SubmittedBuilder.aSubmittedEvent()
                    .aggregateId(UUID.fromString(LOAN_ID_EXAMPLE))
                    .aggregateType(AggregateType.AGGREGATE_LOAN)
                    .eventType(EventType.LOAN_SUBMITTED)
                    .document("123456789")
                    .status(LoanStatus.PENDING)
                    .amount(new BigDecimal("5000"))
                    .typeLoan(1L)
                    .period(12)
                    .totalMonthlyDebt(new BigDecimal("416.67"))
                    .build();

    @BeforeEach
    void setup() {
        loanUseCase = Mockito.mock(LoanUseCase.class);
        loanTypeUseCase = Mockito.mock(LoanTypeUseCase.class);
        eventStoreRepository = Mockito.mock(EventStoreRepository.class);
        loanReadUseCase = Mockito.mock(LoanReadUseCase.class);
        eventBus = Mockito.mock(EventBus.class);
        outboxHandler = Mockito.mock(IOutboxHandler.class);

        orchestratorUseCase = new OrchestratorUseCase(
                loanTypeUseCase,
                loanUseCase,
                eventStoreRepository,
                loanReadUseCase,
                eventBus,
                outboxHandler
        );
    }


    @Test
    void shouldBePersisted_becauseApplicationLoanIsValid () {
        Loan loanMock = Mockito.mock(Loan.class);

        LoanApplicationSubmittedEvent submittedEvent = LoanApplicationSubmittedEvent
                .SubmittedBuilder.aSubmittedEvent()
                .aggregateId(UUID.fromString(LOAN_ID_EXAMPLE))
                .build();

        Mockito.when(loanMock.getUncommittedEvents())
                .thenReturn(List.of(submittedEvent));

        Mockito.when(loanUseCase.checkApplication(Mockito.any()))
                .thenReturn(Mono.just(loanMock));

        Mockito.when(loanUseCase.verifyOwnershipCustomer(loanMock))
                        .thenReturn(Mono.just(loanMock));

        Mockito.when(loanUseCase.markAsPending(loanMock))
                .thenReturn(Mono.just(loanMock));

        Mockito.when(eventStoreRepository.saveAll(Mockito.anyList()))
                .thenReturn(Mono.empty());

        Mockito.doNothing()
                .when(eventBus).publish(Mockito.any());

        CreateApplicationLoanCommand createApplicationLoanCommand =
                new CreateApplicationLoanCommand("103", BigDecimal.valueOf(4_000_000), 1L, 1,6);

        var response = orchestratorUseCase.applicationLoan(createApplicationLoanCommand);

        StepVerifier.create(response)
                .expectNext(LOAN_STATUS_PENDING)
                .verifyComplete();

        Mockito.verify(eventStoreRepository, Mockito.times(1)).saveAll(List.of(submittedEvent));
        Mockito.verify(eventBus, Mockito.times(1)).publish(submittedEvent);
    }

    @Test
    void shouldThrowException_decisionIsNull () {
        ResolveApplicationLoanCommand command = new ResolveApplicationLoanCommand(
                LOAN_ID_EXAMPLE,
                null,
                ""
        );

        var response = orchestratorUseCase.decisionLoan(command);
        StepVerifier.create(response)
                .expectErrorSatisfies(throwable -> {
                    assertEquals("Decision must be mandatory", throwable.getMessage());
                    assertInstanceOf(LoanDomainException.class, throwable);
                })
                .verify();
    }

    @Test
    void shouldThrowException_decisionIsUnknown () {
        ResolveApplicationLoanCommand command = new ResolveApplicationLoanCommand(
                LOAN_ID_EXAMPLE,
                "UNKNOWN",
                ""
        );

        var response = orchestratorUseCase.decisionLoan(command);
        StepVerifier.create(response)
                .expectErrorSatisfies(throwable -> {
                    assertEquals("Unknown decision type", throwable.getMessage());
                    assertInstanceOf(LoanDomainException.class, throwable);
                })
                .verify();
    }

    @Test
    void shouldThrowException_LoanIdIsNull () {
        ResolveApplicationLoanCommand command = new ResolveApplicationLoanCommand(
                null,
                "REJECTED",
                ""
        );

        var response = orchestratorUseCase.decisionLoan(command);
        StepVerifier.create(response)
                .expectErrorSatisfies(throwable -> {
                    assertEquals("Loan Id must be provided", throwable.getMessage());
                    assertInstanceOf(LoanDomainException.class, throwable);
                })
                .verify();
    }

    @Test
    void shouldThrowException_UnknownEvent () {
        UUID aggregateId = UUID.fromString(LOAN_ID_EXAMPLE);
        Mockito.when(eventStoreRepository.findByAggregateId(aggregateId))
                .thenReturn(Flux.just(new UnknownLoanEvent()));

        Mockito.when(loanUseCase.rehydrate(Mockito.anyList()))
                .thenReturn(Mono.error(new LoanDomainException("Unknown event type")));

        ResolveApplicationLoanCommand command = new ResolveApplicationLoanCommand(
                LOAN_ID_EXAMPLE,
                "APPROVED",
                ""
        );

        var response = orchestratorUseCase.decisionLoan(command);
        StepVerifier.create(response)
                .expectErrorSatisfies(throwable -> {
                    assertEquals("Unknown event type", throwable.getMessage());
                    assertInstanceOf(LoanDomainException.class, throwable);
                })
                .verify();
    }


    @Test
    void shouldBeApproved_sendEventApproved () {
        Loan loanMock = Mockito.mock(Loan.class);
        UUID aggregateId = UUID.fromString(LOAN_ID_EXAMPLE);

        LoanResolutionApprovedEvent approvedEvent =
                LoanResolutionApprovedEvent.ApprovedBuilder.anApprovedEvent()
                        .approvedBy("Advisor@credit.com")
                        .aggregateId(aggregateId)
                        .reason("OK")
                        .build();

        Mockito.when(loanMock.getLoanStatus())
                .thenReturn(LoanStatus.APPROVED);

        Mockito.when(loanUseCase.rehydrate(List.of(SUBMITTED_EVENT)))
                .thenReturn(Mono.just(loanMock));

        Mockito.when(loanUseCase.loadUsername(LOAN_STATUS_PENDING))
                .thenReturn(Mono.just(loanMock));

        Mockito.when(loanUseCase.approvedLoan(loanMock, "OK"))
                        .thenReturn(Mono.just(loanMock));

        Mockito.when(loanMock.getUncommittedEvents())
                .thenReturn(List.of(approvedEvent));

        Mockito.when(eventStoreRepository.findByAggregateId(aggregateId))
                .thenReturn(Flux.just(SUBMITTED_EVENT));

        Mockito.when(eventStoreRepository.saveAll(Mockito.anyList()))
                .thenReturn(Mono.empty());

        ResolveApplicationLoanCommand command = new ResolveApplicationLoanCommand(
                LOAN_ID_EXAMPLE,
                "APPROVED",
                "OK"
        );

        var response = orchestratorUseCase.decisionLoan(command);

        StepVerifier.create(response)
                .expectNextMatches(loan -> loan.getLoanStatus().equals(LoanStatus.APPROVED))
                .verifyComplete();

        Mockito.verify(eventStoreRepository, Mockito.times(1)).saveAll(List.of(approvedEvent));
        Mockito.verify(eventBus, Mockito.times(1)).publish(approvedEvent);
    }

}
