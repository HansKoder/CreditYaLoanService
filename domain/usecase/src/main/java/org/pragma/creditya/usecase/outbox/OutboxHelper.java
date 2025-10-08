package org.pragma.creditya.usecase.outbox;

import org.pragma.creditya.model.loan.event.LoanEvent;
import org.pragma.creditya.usecase.outbox.payload.OutboxPayload;

public class OutboxHelper {

    public static LoanOutboxMessage toOutboxMessage(LoanEvent event, OutboxPayload payload) {
        System.out.println("[domain.outbox.helper] (toOutboxMessage) payload=[ event:{" + event+ "} ]");

        OutboxPayload.class.getSimpleName();

        return LoanOutboxMessage.builder()
                .aggregateId(event.getAggregateId())
                .aggregateName(event.getAggregateType().name())
                .type(payload.getType())
                // .type(event.getEventType().getEventClass().getSimpleName())
                .status(OutboxStatus.STARTED)
                .build();
    }

    // match per each
}
