package org.pragma.creditya.outbox.strategy.notification;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.pragma.creditya.model.customer.entity.Customer;
import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loan.event.*;
import org.pragma.creditya.model.loan.event.payload.ApplicationApprovedEvent;
import org.pragma.creditya.model.loan.event.payload.ApplicationRejectedEvent;
import org.pragma.creditya.model.loan.valueobject.LoanStatus;
import org.pragma.creditya.usecase.outbox.payload.NotificationOutboxPayload;
import org.pragma.creditya.usecase.outbox.strategy.notification.NotificationMapper;

import java.util.UUID;


import static org.junit.jupiter.api.Assertions.*;

public class NotificationMapperTest {

    private final String AGGREGATE_ID = "c4fc5172-1cd3-4dfa-aa71-456b50bc9089";

    @DisplayName("Should be mapped outbox payload when Loan was approved ")
    @Test
    void shouldBeMappedToPayload_whenIsApproved () {
        Customer customer = Customer.CustomerBuilder.aCustomer()
                .name("Doe")
                .email("doe@gmail.com")
                .build();

        Loan loan = Loan.LoanBuilder.aLoan()
                .id(UUID.fromString(AGGREGATE_ID))
                .loanStatus(LoanStatus.APPROVED)
                .build();

        var payload = ApplicationApprovedEvent.builder()
                .approvedBy("doe")
                .build();

        var approvedEvent = LoanEvent
                .builder()
                .aggregateId(UUID.fromString(AGGREGATE_ID))
                .payload(payload)
                .build();

        NotificationOutboxPayload outboxPayload = NotificationMapper.toPayload(
                loan, approvedEvent, customer
        );

        assertNotNull(outboxPayload);
        assertEquals("doe@gmail.com", outboxPayload.getDestination());

        assertFalse(outboxPayload.getMessage().isBlank());
        assertTrue(outboxPayload.getMessage().contains("Hello, Dear Doe"));
        assertTrue(outboxPayload.getMessage().contains("Congratulations! Your loan with the code c4fc5172-1cd3-4dfa-aa71-456b50bc9089 was approved successfully. "));
        assertTrue(outboxPayload.getMessage().contains("Best regards"));
        assertTrue(outboxPayload.getMessage().contains("If you have any questions, please do not hesitate to contact our support team for further details or assistance."));
    }

    @DisplayName("Should be mapped outbox payload when Loan was approved ")
    @Test
    void shouldBeMappedToPayload_whenIsRejected () {
        Customer customer = Customer.CustomerBuilder.aCustomer()
                .name("Doe")
                .email("doe@gmail.com")
                .build();

        Loan loan = Loan.LoanBuilder.aLoan()
                .id(UUID.fromString(AGGREGATE_ID))
                .loanStatus(LoanStatus.REJECTED)
                .build();

        var payload = ApplicationRejectedEvent.builder()
                .rejectedBy("doe")
                .build();

        var rejectedEvent = LoanEvent
                .builder()
                .aggregateId(UUID.fromString(AGGREGATE_ID))
                .payload(payload)
                .build();

        NotificationOutboxPayload outboxPayload = NotificationMapper.toPayload(
                loan, rejectedEvent, customer
        );

        assertNotNull(outboxPayload);
        assertEquals("doe@gmail.com", outboxPayload.getDestination());

        assertFalse(outboxPayload.getMessage().isBlank());
        assertTrue(outboxPayload.getMessage().contains("Hello, Dear Doe"));
        assertTrue(outboxPayload.getMessage().contains("Sorry, your loan with the code c4fc5172-1cd3-4dfa-aa71-456b50bc9089 was rejected"));
        assertTrue(outboxPayload.getMessage().contains("If you have any questions, please do not hesitate to contact our support team for further details or assistance."));
    }

}
