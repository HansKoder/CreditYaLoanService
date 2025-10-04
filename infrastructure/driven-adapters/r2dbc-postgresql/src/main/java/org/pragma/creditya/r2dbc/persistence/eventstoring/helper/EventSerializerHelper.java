package org.pragma.creditya.r2dbc.persistence.eventstoring.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.pragma.creditya.model.loan.event.*;
import org.pragma.creditya.model.loan.event.payload.ApplicationApprovedEvent;
import org.pragma.creditya.model.loan.event.payload.ApplicationRejectedEvent;
import org.pragma.creditya.model.loan.event.payload.ApplicationSubmittedEvent;
import org.pragma.creditya.model.loan.event.payload.LoanEventPayload;
import org.pragma.creditya.r2dbc.persistence.eventstoring.entity.EventEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventSerializerHelper {

    private final ObjectMapper objectMapper;
    private final Logger log = LoggerFactory.getLogger(EventSerializerHelper.class);

    public String serialize(LoanEvent event) {
        log.info("[infra.r2dbc] (object-mapper) serialize: {}", event);
        try {
            LoanEventPayload data = event.getPayload();
            log.info("[infra.r2dbc] (object-mapper) loanEventPayload: {}", data);
            String payload = objectMapper.writeValueAsString(data);
            log.info("[infra.r2dbc] (object-mapper) serialized this is the payload: {}", payload);
            return payload;
        } catch (Exception e) {
            throw new RuntimeException("Error serializing event", e);
        }
    }

    public LoanEventPayload deserialize(EventEntity data) {
        log.info("[infra.r2dbc.event.sourcing] (deserialize-01) deserialize from jpa to event (entity) payload=[ data:{} ]", data);

        try {
            Class<? extends LoanEventPayload> clazz = resolveEventClass(data.getEventType());
            LoanEventPayload event = objectMapper.readValue(data.getPayload().asString(), clazz);
            log.info("[infra.r2dbc.event.sourcing] (deserialize-02) event deserialized response=[ event:{} ]", event);
            return event;
        } catch (Exception e) {
            log.info("[infra.r2dbc.event.sourcing] (deserialize-02) unexpected error when is  deserialized, response=[ error:{} ]", e.getMessage());
            throw new RuntimeException("Error deserializing payload=[ eventType:" + data.getEventType() + ", error:" + e.getMessage() + ", ]", e);
        }
    }

    public Class<? extends LoanEventPayload> resolveEventClass(String eventType) {
        return switch (eventType) {
            case "ApplicationSubmittedEvent" -> ApplicationSubmittedEvent.class;
            case "ApplicationApprovedEvent" -> ApplicationApprovedEvent.class;
            case "ApplicationRejectedEvent" -> ApplicationRejectedEvent.class;
            default -> throw new IllegalArgumentException("Unknown eventType: " + eventType);
        };
    }

}
