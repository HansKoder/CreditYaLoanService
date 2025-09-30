package org.pragma.creditya.outbox.strategy.notification;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loan.entity.CustomerRead;
import org.pragma.creditya.model.loan.event.LoanApplicationSubmittedEvent;
import org.pragma.creditya.model.loan.event.LoanResolutionApprovedEvent;
import org.pragma.creditya.model.loan.event.LoanResolutionRejectedEvent;
import org.pragma.creditya.model.loan.gateways.CustomerClient;
import org.pragma.creditya.model.loan.valueobject.LoanStatus;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class NotificationHandlerTest {

    @InjectMocks
    private NotificationHandler notificationHandler;

    @Mock
    private CustomerClient customerClient;

    @BeforeEach
    void setup () {
        notificationHandler = new NotificationHandler(customerClient);
    }

    private final String AGGREGATE_ID = "c4fc5172-1cd3-4dfa-aa71-456b50bc9089";

    @Test
    void shouldBeFalse_becauseEventIsSubmitted () {
        LoanApplicationSubmittedEvent submittedEvent = LoanApplicationSubmittedEvent.SubmittedBuilder
                .aSubmittedEvent()
                .aggregateId(UUID.fromString(AGGREGATE_ID))
                .build();

        assertFalse(notificationHandler.apply(submittedEvent));
    }

    @Test
    void shouldBeTrue_becauseEventIsApprovedLoan () {
        LoanResolutionApprovedEvent approvedEvent = LoanResolutionApprovedEvent.ApprovedBuilder
                .anApprovedEvent()
                .aggregateId(UUID.fromString(AGGREGATE_ID))
                .build();

        assertTrue(notificationHandler.apply(approvedEvent));
    }

    @Test
    void shouldBeTrue_becauseEventIsRejectedLoan () {
        LoanResolutionRejectedEvent rejectedEvent = LoanResolutionRejectedEvent.RejectedBuilder
                .aRejectedEvent()
                .aggregateId(UUID.fromString(AGGREGATE_ID))
                .build();

        assertTrue(notificationHandler.apply(rejectedEvent));
    }

    @Test
    void shouldBeMappedToOutboxMessage_whenIsApproved () {
        CustomerRead customer = CustomerRead.builder()
                .email("doe@gmail.com")
                .name("Doe")
                .document("123")
                .build();

        when(customerClient.getCustomerByDocument(anyString()))
                .thenReturn(Mono.just(customer));

        LoanResolutionApprovedEvent approvedEvent = LoanResolutionApprovedEvent.ApprovedBuilder
                .anApprovedEvent()
                .aggregateId(UUID.fromString(AGGREGATE_ID))
                .status(LoanStatus.APPROVED)
                .build();

        Loan domain = Loan.LoanBuilder.aLoan()
                .id(UUID.fromString(AGGREGATE_ID))
                .loanStatus(LoanStatus.APPROVED)
                .document("123")
                .build();

        var payload = notificationHandler.handler(domain, approvedEvent);

        StepVerifier.create(payload)
                .expectNextMatches(result ->
                        result.getDestination().equals("doe@gmail.com") &&
                                !result.getMessage().isBlank() &&
                                result.getType().equals("EMAIL")
                )
                .verifyComplete();
    }

}
