package org.pragma.creditya.usecase.outbox;

import org.pragma.creditya.model.loan.event.LoanEvent;

public class OutboxHelper {

    public static LoanOutboxMessage toOutboxMessage(LoanEvent event) {
        System.out.println("[domain.outbox.helper] (toOutboxMessage) payload=[ event:{" + event+ "} ]");
        return LoanOutboxMessage.builder()
                .aggregateId(event.getAggregateId())
                .aggregateName(event.getAggregateType().name())
                .type(event.getEventType().getEventClass().getSimpleName())
                .status(OutboxStatus.STARTED)
                .build();
    }
}
