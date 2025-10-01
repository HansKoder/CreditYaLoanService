package org.pragma.creditya.model.loan.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.pragma.creditya.model.loan.valueobject.LoanStatus;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class LoanApplicationSubmittedEventTest {

    private final UUID LOAN_ID_EXAMPLE = UUID.fromString("e36702e6-78d0-4368-a191-292f53c8141c");

    @DisplayName("Should be returned with successful the event type")
    @Test
    void shouldBeReturnedWithSuccessfulEventType () {
        LoanApplicationSubmittedEvent event = LoanApplicationSubmittedEvent
                .SubmittedBuilder
                .aSubmittedEvent()
                .aggregateId(LOAN_ID_EXAMPLE)
                .aggregateType(AggregateType.AGGREGATE_LOAN)
                .eventType(EventType.LOAN_SUBMITTED)
                .status(LoanStatus.PENDING)
                .document("123")
                .typeLoan(1L)
                .build();

        assertNotNull(event.getEventType());
        assertEquals("LOAN_SUBMITTED", event.getEventType().name());
        assertEquals("LoanApplicationSubmittedEvent", event.getEventType().getEventClass().getSimpleName());
    }

}
