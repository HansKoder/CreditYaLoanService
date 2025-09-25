package org.pragma.creditya.r2dbc.persistence.outbox.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.pragma.creditya.model.loan.event.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OutboxSerializerHelper {

    private final ObjectMapper objectMapper;
    private final Logger log = LoggerFactory.getLogger(OutboxSerializerHelper.class);

    public String serializePayload(Object payload) {
        log.info("[infra.r2dbc.outbox] (serialize) payload: {}", payload);
        try {
            String result = objectMapper.writeValueAsString(payload);
            log.info("[infra.r2dbc.outbox] (serialize) serialized this is the payload: {}", payload);
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Error Outbox serialize event", e);
        }
    }

    public <T> T deserializePayload(String payload, Class<T> clazz) {
        log.info("[infra.r2dbc.outbox] (deserialize) payloadString={}, ClassName={}",
                payload, clazz.getSimpleName());

        try {
            T event = objectMapper.readValue(payload, clazz);
            log.info("[infra.r2dbc] (object-mapper) deserialized payload: {}", event);
            return event;
        } catch (Exception e) {
            log.info("[infra.r2dbc] (object-mapper) deserialized payload: {}, className: {}", payload, clazz.getSimpleName());
            throw new RuntimeException("Error deserializing payload=" + payload, e);
        }
    }


}
