package org.pragma.creditya.model.outbox;

import lombok.*;

import java.util.UUID;

@ToString
@Builder
@Getter
@Setter
public class LoanOutboxMessage{

    private UUID id;
    private UUID aggregateId;
    private String type;
    private String aggregateName;
    private OutboxStatus status;
    private String payload;

}
