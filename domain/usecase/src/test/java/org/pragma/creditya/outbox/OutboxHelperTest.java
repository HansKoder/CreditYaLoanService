package org.pragma.creditya.outbox;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.pragma.creditya.model.loan.event.LoanApplicationSubmittedEvent;
import org.pragma.creditya.model.loan.event.LoanEvent;
import org.pragma.creditya.model.loan.event.LoanResolutionApprovedEvent;
import org.pragma.creditya.model.loan.valueobject.LoanStatus;
import org.pragma.creditya.model.outbox.LoanOutboxMessage;
import org.pragma.creditya.model.outbox.OutboxStatus;

import java.math.BigDecimal;
import java.util.UUID;


import static org.junit.jupiter.api.Assertions.*;

public class OutboxHelperTest {

    private String EVENT_ID = "e61aba47-75bf-4e79-b04f-a646aac86f89";
    private String AGGREGATE_ID = "c4fc5172-1cd3-4dfa-aa71-456b50bc9089";

    @Test
    @DisplayName("Should Be mapped with successful with the required fields outbox pattern")
    void shouldBeMappedCorrectly_FromSubmittedToOutboxMessage () {
        LoanEvent submittedEvent = LoanApplicationSubmittedEvent.LoanBuilder.aLoanApplicationSubmitted()
                .eventId(UUID.fromString(EVENT_ID))
                .aggregateId(UUID.fromString(AGGREGATE_ID))
                .aggregateType("LOAN")
                .eventType("SUBMITTED_EVENT")
                .status(LoanStatus.PENDING.name())
                .document("123")
                .amount(BigDecimal.valueOf(100))
                .typeLoan(1L)
                .build();
        LoanOutboxMessage outboxMessage = OutboxHelper.toOutboxMessage(submittedEvent);

        assertEquals(AGGREGATE_ID, outboxMessage.getAggregateId().toString());
        assertEquals("LOAN", outboxMessage.getAggregateName());
        assertEquals("LoanApplicationSubmittedEvent", outboxMessage.getType());
        assertEquals(OutboxStatus.STARTED, outboxMessage.getStatus());
    }

    @Test
    @DisplayName("Should Be mapped with successful with the required fields outbox pattern")
    void shouldBeMappedCorrectly_FromSubmittedToOutboxMessage () {
        LoanEvent submittedEvent = LoanResolutionApprovedEvent.LoanBuilder.aLoanResolutionApproved()
                .eventId(UUID.fromString(EVENT_ID))
                .aggregateId(UUID.fromString(AGGREGATE_ID))
                .aggregateType("LOAN")
                .eventType("SUBMITTED_EVENT")
                .status(LoanStatus.PENDING.name())
                .document("123")
                .amount(BigDecimal.valueOf(100))
                .typeLoan(1L)
                .build();
        LoanOutboxMessage outboxMessage = OutboxHelper.toOutboxMessage(submittedEvent);

        assertEquals(AGGREGATE_ID, outboxMessage.getAggregateId().toString());
        assertEquals("LOAN", outboxMessage.getAggregateName());
        assertEquals("LoanApplicationSubmittedEvent", outboxMessage.getType());
        assertEquals(OutboxStatus.STARTED, outboxMessage.getStatus());
    }

}
