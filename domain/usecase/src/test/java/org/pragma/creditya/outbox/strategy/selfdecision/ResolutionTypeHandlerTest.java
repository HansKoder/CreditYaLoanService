package org.pragma.creditya.outbox.strategy.selfdecision;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loan.entity.CustomerRead;
import org.pragma.creditya.model.loan.event.AggregateType;
import org.pragma.creditya.model.loan.event.EventType;
import org.pragma.creditya.model.loan.event.LoanApplicationSubmittedEvent;
import org.pragma.creditya.model.customer.gateway.CustomerRepository;
import org.pragma.creditya.model.loan.valueobject.LoanStatus;
import org.pragma.creditya.usecase.query.repository.LoanReadRepository;
import org.pragma.creditya.usecase.outbox.payload.DecisionLoanOutboxPayload;
import org.pragma.creditya.usecase.outbox.strategy.selfdecision.SelfDecisionHandler;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ResolutionTypeHandlerTest {

    @InjectMocks
    private SelfDecisionHandler handler;

    @Mock
    private CustomerRepository customerClient;

    @Mock
    private LoanReadRepository loanReadRepository;

    private final String AGGREGATE_ID = "c4fc5172-1cd3-4dfa-aa71-456b50bc9089";

    @BeforeEach
    void setup () {
        handler = new SelfDecisionHandler(customerClient, loanReadRepository);
    }

    @Test
    void shouldBeTrue_whenSubmittedTypeIsSelf () {
        LoanApplicationSubmittedEvent submittedEvent = LoanApplicationSubmittedEvent.SubmittedBuilder
                .aSubmittedEvent()
                .aggregateId(UUID.fromString(AGGREGATE_ID))
                .typeSubmitted(ApplicationSubmittedType.SELF_DECISION)
                .build();

        assertTrue(handler.apply(submittedEvent));
    }
    @Test
    void shouldBeFalse_whenSubmittedTypeIsManual () {
        LoanApplicationSubmittedEvent submittedEvent = LoanApplicationSubmittedEvent.SubmittedBuilder
                .aSubmittedEvent()
                .aggregateId(UUID.fromString(AGGREGATE_ID))
                .typeSubmitted(ApplicationSubmittedType.MANUAL_DECISION)
                .build();

        assertFalse(handler.apply(submittedEvent));
    }

    @Test
    void should () {
        CustomerRead customer = CustomerRead.builder()
                .email("doe@gmail.com")
                .name("Doe")
                .document("123")
                .baseSalary(BigDecimal.valueOf(2500))
                .build();

        when(customerClient.getCustomerByDocument(anyString()))
                .thenReturn(Mono.just(customer));

        when(loanReadRepository.getActiveDebts("123"))
                .thenReturn(Flux.empty());

        LoanApplicationSubmittedEvent submittedEvent = LoanApplicationSubmittedEvent.SubmittedBuilder
                .aSubmittedEvent()
                .aggregateId(UUID.fromString(AGGREGATE_ID))
                .typeLoan(1L)
                .eventType(EventType.LOAN_SUBMITTED)
                .aggregateType(AggregateType.AGGREGATE_LOAN)
                .typeSubmitted(ApplicationSubmittedType.SELF_DECISION)
                .status(LoanStatus.PENDING)
                .document("123")
                .amount(BigDecimal.valueOf(1500))
                .period(10)
                .build();

        Loan domain = Loan.LoanBuilder
                .aLoan()
                .id(UUID.fromString(AGGREGATE_ID))
                .loanStatus(LoanStatus.PENDING)
                .document("123")
                .amount(BigDecimal.valueOf(1500))
                .build();

        var payload = handler.handler(domain, submittedEvent);

        StepVerifier.create(payload)
                .assertNext(outboxPayload -> {
                    assertInstanceOf(DecisionLoanOutboxPayload.class, outboxPayload);

                    DecisionLoanOutboxPayload result = (DecisionLoanOutboxPayload) outboxPayload;

                    assertEquals(AGGREGATE_ID, result.getCurrentLoanId());
                    assertEquals(BigDecimal.valueOf(1500), result.getCurrentLoanAmount());
                    assertEquals(0, result.getDebts().size());
                    assertEquals(BigDecimal.valueOf(2500), result.getCustomerSalary());
                })
                .verifyComplete();
    }


}
