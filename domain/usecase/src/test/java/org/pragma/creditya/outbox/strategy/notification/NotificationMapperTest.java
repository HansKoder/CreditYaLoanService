package org.pragma.creditya.outbox.strategy.notification;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loan.entity.CustomerRead;
import org.pragma.creditya.model.loan.event.LoanResolutionApprovedEvent;
import org.pragma.creditya.model.loan.event.LoanResolutionRejectedEvent;
import org.pragma.creditya.model.loan.valueobject.LoanStatus;
import org.pragma.creditya.outbox.payload.NotificationOutboxPayload;

import java.util.UUID;


import static org.junit.jupiter.api.Assertions.*;

public class NotificationMapperTest {

    private final String AGGREGATE_ID = "c4fc5172-1cd3-4dfa-aa71-456b50bc9089";

    @DisplayName("Should be mapped outbox payload when Loan was approved ")
    @Test
    void shouldBeMappedToPayload_whenIsApproved () {
        CustomerRead customer = CustomerRead.builder()
                .name("Doe")
                .email("doe@gmail.com")
                .build();

        Loan loan = Loan.LoanBuilder.aLoan()
                .id(UUID.fromString(AGGREGATE_ID))
                .loanStatus(LoanStatus.APPROVED)
                .build();

        LoanResolutionApprovedEvent approvedEvent = LoanResolutionApprovedEvent.ApprovedBuilder
                .anApprovedEvent()
                .aggregateId(UUID.fromString(AGGREGATE_ID))
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
        CustomerRead customer = CustomerRead.builder()
                .name("Doe")
                .email("doe@gmail.com")
                .build();

        Loan loan = Loan.LoanBuilder.aLoan()
                .id(UUID.fromString(AGGREGATE_ID))
                .loanStatus(LoanStatus.REJECTED)
                .build();

        LoanResolutionRejectedEvent rejectedEvent = LoanResolutionRejectedEvent.RejectedBuilder
                .aRejectedEvent()
                .aggregateId(UUID.fromString(AGGREGATE_ID))
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
