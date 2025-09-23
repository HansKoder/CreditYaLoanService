package org.pragma.creditya.sqs.sender.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SerializationHelper {

    private final ObjectMapper objectMapper;
    private final Logger log = LoggerFactory.getLogger(SerializationHelper.class);

    public String serialize(Object data) {
        log.info("[infra.sqs.helper] (serialize) Data: {}", data);
        try {
            String payload = objectMapper.writeValueAsString(data);
            log.info("[infra.sqs.helper] (serialize) serialized, this is the final payload: {}", payload);
            return payload;
        } catch (Exception e) {
            throw new RuntimeException("Error serialization: ", e);
        }
    }


}
