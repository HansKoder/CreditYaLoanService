package org.pragma.creditya.r2dbc.persistence.outbox.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.pragma.creditya.model.loan.event.LoanApplicationSubmittedEvent;
import org.pragma.creditya.model.loan.event.LoanEvent;
import org.pragma.creditya.model.loan.event.LoanResolutionApprovedEvent;
import org.pragma.creditya.model.loan.event.LoanResolutionRejectedEvent;
import org.pragma.creditya.r2dbc.persistence.eventstoring.entity.EventEntity;
import org.pragma.creditya.r2dbc.persistence.eventstoring.helper.EventSerializerHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OutboxSerializerHelper {

    private final ObjectMapper objectMapper;
    private final Logger log = LoggerFactory.getLogger(OutboxSerializerHelper.class);

    public String serialize(LoanEvent event) {
        log.info("[infra.r2dbc] (outbox-serialize) event: {}", event);
        try {
            String payload = objectMapper.writeValueAsString(event);
            log.info("[infra.r2dbc] (outbox-serialize) serialized this is the payload: {}", payload);
            return payload;
        } catch (Exception e) {
            throw new RuntimeException("Error Outbox serialize event", e);
        }
    }

    public LoanEvent deserialize(EventEntity entity) {
        log.info("[infra.r2dbc] (object-mapper) deserialize eventType={}, aggregateId={}",
                entity.getEventType(), entity.getAggregateId());

        try {
            Class<? extends LoanEvent> clazz = resolveEventClass(entity.getEventType());
            LoanEvent event = objectMapper.readValue(entity.getPayload().asString(), clazz);
            log.info("[infra.r2dbc] (object-mapper) deserialized payload: {}", event);
            return event;
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing eventType=" + entity.getEventType(), e);
        }
    }

    private Class<? extends LoanEvent> resolveEventClass(String eventType) {
        return switch (eventType) {
            case "LoanApplicationSubmittedEvent" -> LoanApplicationSubmittedEvent.class;
            default -> throw new IllegalArgumentException("Unknown eventType: " + eventType);
        };
    }

}
