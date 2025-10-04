package org.pragma.creditya.outbox.strategy.notification;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pragma.creditya.model.customer.entity.Customer;
import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loan.event.*;
import org.pragma.creditya.model.customer.gateway.CustomerRepository;
import org.pragma.creditya.model.loan.valueobject.LoanStatus;
import org.pragma.creditya.usecase.outbox.payload.NotificationOutboxPayload;
import org.pragma.creditya.usecase.outbox.strategy.notification.NotificationHandler;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class NotificationHandlerTest {

    @InjectMocks
    private NotificationHandler notificationHandler;

    @Mock
    private CustomerRepository customerClient;

    @BeforeEach
    void setup () {
        notificationHandler = new NotificationHandler(customerClient);
    }
    private final String AGGREGATE_ID = "c4fc5172-1cd3-4dfa-aa71-456b50bc9089";

    @Test
    void shouldBeFalse_becauseEventIsSubmitted () {
        var submittedEvent = ApplicationSubmittedEvent
                .builder()
                .build();

        assertFalse(notificationHandler.apply(submittedEvent));
    }

    @Test
    void shouldBeTrue_becauseEventIsApprovedLoan () {
        var approvedEvent = ApplicationApprovedEvent.builder()
                .build();

        assertTrue(notificationHandler.apply(approvedEvent));
    }

    @Test
    void shouldBeTrue_becauseEventIsRejectedLoan () {
        var rejectedEvent = ApplicationRejectedEvent.builder()
                .build();

        assertTrue(notificationHandler.apply(rejectedEvent));
    }

    @Test
    void shouldBeMappedToOutboxMessage_whenIsApproved () {
        Customer customer = Customer.CustomerBuilder.aCustomer()
                .email("doe@gmail.com")
                .name("Doe")
                .build();

        when(customerClient.getCustomerByDocument(any()))
                .thenReturn(Mono.just(customer));

        var approvedPayload = ApplicationApprovedEvent.builder()
                .approvedBy("doe")
                .reason("OK")
                .status(LoanStatus.APPROVED)
                .build();

        var approvedEvent = LoanEvent.builder()
                .aggregateId(UUID.fromString(AGGREGATE_ID))
                .payload(approvedPayload)
                .build();

        Loan domain = Loan.LoanBuilder.aLoan()
                .id(UUID.fromString(AGGREGATE_ID))
                .loanStatus(LoanStatus.APPROVED)
                .document("123")
                .build();

        var payload = notificationHandler.handler(domain, approvedEvent);

        StepVerifier.create(payload)
                .assertNext(outboxPayload -> {
                    assertInstanceOf(NotificationOutboxPayload.class, outboxPayload);

                    NotificationOutboxPayload notificationPayload = (NotificationOutboxPayload) outboxPayload;

                    assertEquals("EMAIL", notificationPayload.getType());
                    assertEquals("Loan Decision", notificationPayload.getSubject());
                    assertEquals(customer.getEmail(), notificationPayload.getDestination());
                    assertTrue(notificationPayload.getMessage()
                            .contains("Congratulations! Your loan with the code c4fc5172-1cd3-4dfa-aa71-456b50bc9089 was approved successfully."));
                })
                .verifyComplete();
    }

}
