package org.pragma.creditya.usecase.outbox.payload;

import org.pragma.creditya.usecase.outbox.OutboxTypeEvent;

public interface OutboxPayload {

    OutboxTypeEvent getType();
}
