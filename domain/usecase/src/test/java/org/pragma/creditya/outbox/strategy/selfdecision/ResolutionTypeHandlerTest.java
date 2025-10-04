package org.pragma.creditya.outbox.strategy.selfdecision;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pragma.creditya.model.customer.entity.Customer;
import org.pragma.creditya.model.customer.valueobject.Document;
import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loan.event.*;
import org.pragma.creditya.model.customer.gateway.CustomerRepository;
import org.pragma.creditya.model.loan.valueobject.LoanStatus;
import org.pragma.creditya.model.loantype.valueobject.ResolutionType;
import org.pragma.creditya.model.shared.domain.model.valueobject.Amount;
import org.pragma.creditya.usecase.query.repository.LoanReadRepository;
import org.pragma.creditya.usecase.outbox.payload.DecisionLoanOutboxPayload;
import org.pragma.creditya.usecase.outbox.strategy.selfdecision.SelfDecisionHandler;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
        var payload = ApplicationSubmittedEvent.builder()
                .resolutionType(ResolutionType.SELF_DECISION)
                .build();

        assertTrue(handler.apply(payload));
    }
    @Test
    void shouldBeFalse_whenSubmittedTypeIsManual () {
        var payload = ApplicationSubmittedEvent.builder()
                .resolutionType(ResolutionType.MANUAL_DECISION)
                .build();

        assertFalse(handler.apply(payload));
    }

    @Test
    void should () {
        Customer customer = Customer.CustomerBuilder.aCustomer()
                .email("doe@gmail.com")
                .name("Doe")
                .id(new Document("123"))
                .baseSalary(new Amount(BigDecimal.valueOf(2500)))
                .build();

        when(customerClient.getCustomerByDocument(any()))
                .thenReturn(Mono.just(customer));

        when(loanReadRepository.getActiveDebts(new Document("123")))
                .thenReturn(Flux.empty());

        var submittedPayload = ApplicationSubmittedEvent.builder()
                .typeLoan(1L)
                .resolutionType(ResolutionType.SELF_DECISION)
                .status(LoanStatus.PENDING)
                .document("123")
                .amount(BigDecimal.valueOf(1500))
                .period(10)
                .build();

        var submittedEvent = LoanEvent.builder()
                .aggregateId(UUID.fromString(AGGREGATE_ID))
                .eventType(EventType.LOAN_SUBMITTED)
                .aggregateType(AggregateType.AGGREGATE_LOAN)
                .payload(submittedPayload)
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
