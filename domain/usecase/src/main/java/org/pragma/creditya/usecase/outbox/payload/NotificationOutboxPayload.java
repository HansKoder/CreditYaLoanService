package org.pragma.creditya.usecase.outbox.payload;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.pragma.creditya.usecase.outbox.OutboxTypeEvent;

@ToString
@Getter
@Setter
@Builder
public class NotificationOutboxPayload implements OutboxPayload {

    private String type;
    private String subject;
    private String message;
    private String destination;

    @Override
    public OutboxTypeEvent getType() {
        return OutboxTypeEvent.NOTIFICATION;
    }

}
