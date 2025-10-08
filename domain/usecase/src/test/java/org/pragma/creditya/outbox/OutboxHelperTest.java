package org.pragma.creditya.outbox;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.pragma.creditya.model.loan.event.*;
import org.pragma.creditya.model.loan.event.payload.ApplicationApprovedEvent;
import org.pragma.creditya.model.loan.event.payload.ApplicationSubmittedEvent;
import org.pragma.creditya.model.loan.valueobject.LoanStatus;
import org.pragma.creditya.usecase.outbox.LoanOutboxMessage;
import org.pragma.creditya.usecase.outbox.OutboxStatus;
import org.pragma.creditya.usecase.outbox.OutboxHelper;

import java.math.BigDecimal;
import java.util.UUID;


import static org.junit.jupiter.api.Assertions.*;

public class OutboxHelperTest {

    private final String EVENT_ID = "e61aba47-75bf-4e79-b04f-a646aac86f89";
    private final String AGGREGATE_ID = "c4fc5172-1cd3-4dfa-aa71-456b50bc9089";

    /**
    @Test
    @DisplayName("Should Be mapped with successful event Submitted with the required fields outbox pattern")
    void shouldBeMappedCorrectly_FromSubmittedToOutboxMessage () {
        ApplicationSubmittedEvent payload = ApplicationSubmittedEvent.builder()
                .status(LoanStatus.PENDING)
                .document("123")
                .amount(BigDecimal.valueOf(100))
                .typeLoan(1L)
                .build();

        LoanEvent submittedEvent = LoanEvent.builder()
                .id(UUID.fromString(EVENT_ID))
                .aggregateId(UUID.fromString(AGGREGATE_ID))
                .aggregateType(AggregateType.AGGREGATE_LOAN)
                .eventType(EventType.LOAN_SUBMITTED)
                .payload(payload)
                .build();

        LoanOutboxMessage outboxMessage = OutboxHelper.toOutboxMessage(submittedEvent);

        assertEquals(AGGREGATE_ID, outboxMessage.getAggregateId().toString());
        assertEquals("AGGREGATE_LOAN", outboxMessage.getAggregateName());
        assertEquals("ApplicationSubmittedEvent", outboxMessage.getType());
        assertEquals(OutboxStatus.STARTED, outboxMessage.getStatus());
    }

    @Test
    @DisplayName("Should Be mapped with successful event Approved with the required fields outbox pattern")
    void shouldBeMappedCorrectly_FromApprovedToOutboxMessage () {
        ApplicationApprovedEvent payload = ApplicationApprovedEvent.builder()
                .status(LoanStatus.APPROVED)
                .approvedBy("self@gmail.com")
                .reason("OK")
                .build();

        LoanEvent submittedEvent = LoanEvent.builder()
                .id(UUID.fromString(EVENT_ID))
                .aggregateId(UUID.fromString(AGGREGATE_ID))
                .aggregateType(AggregateType.AGGREGATE_LOAN)
                .eventType(EventType.LOAN_APPROVED)
                .payload(payload)
                .build();

        LoanOutboxMessage outboxMessage = OutboxHelper.toOutboxMessage(submittedEvent);

        assertEquals(AGGREGATE_ID, outboxMessage.getAggregateId().toString());
        assertEquals("AGGREGATE_LOAN", outboxMessage.getAggregateName());
        assertEquals("ApplicationApprovedEvent", outboxMessage.getType());
        assertEquals(OutboxStatus.STARTED, outboxMessage.getStatus());
    }
     **/


}
