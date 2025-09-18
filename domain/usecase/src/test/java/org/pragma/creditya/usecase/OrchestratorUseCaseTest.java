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
import org.pragma.creditya.model.loan.event.LoanApplicationSubmittedEvent;
import org.pragma.creditya.model.loan.event.LoanEvent;
import org.pragma.creditya.model.loan.event.LoanResolutionApprovedEvent;
import org.pragma.creditya.model.loan.exception.LoanDomainException;
import org.pragma.creditya.model.loan.gateways.EventStoreRepository;
import org.pragma.creditya.model.loan.gateways.OutboxRepository;
import org.pragma.creditya.model.loan.valueobject.LoanStatus;
import org.pragma.creditya.usecase.command.CreateRequestLoanCommand;
import org.pragma.creditya.usecase.command.DecisionLoanCommand;
import org.pragma.creditya.usecase.loan.ILoanUseCase;
import org.pragma.creditya.usecase.loan.LoanUseCase;
import org.pragma.creditya.usecase.loanread.ILoanReadUseCase;
import org.pragma.creditya.usecase.loanread.LoanReadUseCase;
import org.pragma.creditya.usecase.loantype.ILoanTypeUseCase;
import org.pragma.creditya.usecase.loantype.LoanTypeUseCase;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.Instant;
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
    private OutboxRepository outboxRepository;

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
            super(UUID.randomUUID(),
                    1,
                    Instant.now(),
                    "EVENT",
                    "LOAN");
        }
    }

    private final LoanApplicationSubmittedEvent SUBMITTED_EVENT =
            LoanApplicationSubmittedEvent.LoanBuilder.aLoanApplicationSubmitted()
                    .aggregateId(UUID.fromString(LOAN_ID_EXAMPLE))
                    .aggregateType("LOAN")
                    .eventType(LoanApplicationSubmittedEvent.class.getSimpleName())
                    .timestamp(Instant.now())
                    .document("123456789")
                    .status(LoanStatus.PENDING.name())
                    .amount(new BigDecimal("5000"))
                    .typeLoan(1L)
                    .period(12)
                    .baseSalary(new BigDecimal("2000"))
                    .email("customer@test.com")
                    .name("John Doe")
                    .interestRate(0.5)
                    .typeLoanDescription("Personal Loan")
                    .totalMonthlyDebt(new BigDecimal("416.67"))
                    .build();

    @BeforeEach
    void setup() {
        loanUseCase = Mockito.mock(LoanUseCase.class);
        loanTypeUseCase = Mockito.mock(LoanTypeUseCase.class);
        eventStoreRepository = Mockito.mock(EventStoreRepository.class);
        loanReadUseCase = Mockito.mock(LoanReadUseCase.class);
        eventBus = Mockito.mock(EventBus.class);
        outboxRepository = Mockito.mock(OutboxRepository.class);

        orchestratorUseCase = new OrchestratorUseCase(loanTypeUseCase, loanUseCase, eventStoreRepository, loanReadUseCase, eventBus, outboxRepository);
    }


    @Test
    void shouldBePersisted_becauseApplicationLoanIsValid () {
        Loan loanMock = Mockito.mock(Loan.class);

        LoanApplicationSubmittedEvent submittedEvent = LoanApplicationSubmittedEvent.LoanBuilder
                .aLoanApplicationSubmitted()
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

        Mockito.when(loanTypeUseCase.checkLoanTypeAndLoad(loanMock))
                .thenReturn(Mono.just(loanMock));

        Mockito.when(eventStoreRepository.saveAll(Mockito.anyList()))
                .thenReturn(Mono.empty());

        Mockito.doNothing()
                .when(eventBus).publish(Mockito.any());

        CreateRequestLoanCommand createRequestLoanCommand =
                new CreateRequestLoanCommand("103", BigDecimal.valueOf(4_000_000), 1L, 1,6);

        var response = orchestratorUseCase.applicationLoan(createRequestLoanCommand);

        StepVerifier.create(response)
                .expectNext(LOAN_STATUS_PENDING)
                .verifyComplete();

        Mockito.verify(eventStoreRepository, Mockito.times(1)).saveAll(List.of(submittedEvent));
        Mockito.verify(eventBus, Mockito.times(1)).publish(submittedEvent);
    }

    @Test
    void shouldThrowException_decisionIsNull () {
        DecisionLoanCommand command = new DecisionLoanCommand(
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
        DecisionLoanCommand command = new DecisionLoanCommand(
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
        DecisionLoanCommand command = new DecisionLoanCommand(
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

        DecisionLoanCommand command = new DecisionLoanCommand(
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
                LoanResolutionApprovedEvent.LoanBuilder.aLoanResolutionApproved()
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

        DecisionLoanCommand command = new DecisionLoanCommand(
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
