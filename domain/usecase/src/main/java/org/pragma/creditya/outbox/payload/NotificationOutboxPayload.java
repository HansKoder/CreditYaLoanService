package org.pragma.creditya.outbox.payload;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Builder
public class NotificationOutboxPayload {

    private String type;
    private String subject;
    private String message;
    private String destination;


}
