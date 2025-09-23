package org.pragma.creditya.r2dbc.persistence.outbox.mapper;

import io.r2dbc.postgresql.codec.Json;
import org.pragma.creditya.model.loan.event.LoanEvent;
import org.pragma.creditya.r2dbc.persistence.outbox.entity.OutboxEntity;
import org.pragma.creditya.r2dbc.persistence.outbox.entity.OutboxStatus;
import org.pragma.creditya.r2dbc.persistence.outbox.helper.OutboxSerializerHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OutboxMapper {

    private final static Logger log = LoggerFactory.getLogger(OutboxMapper.class);

    public static OutboxEntity fromEventToEntity (LoanEvent event, OutboxSerializerHelper serializerHelper, OutboxStatus status) {
        log.info("[infra.r2dbc.outbox.mapper] (fromEventToEntity) map to persist: {}", event);

        OutboxEntity entity = OutboxEntity.builder()
                .aggregateId(event.getAggregateId().toString())
                .aggregateName(event.getAggregateType())
                .eventType(event.getEventType())
                .status(status)
                .payload(Json.of(serializerHelper.serialize(event)))
                .build();

        // For handling update
        if (event.getId() != null) entity.setId(event.getId());

        log.info("[infra.r2dbc.outbox.mapper] (fromEventToEntity) from event to entity, payload: {}", entity);

        return entity;
    }

    public static LoanEvent fromEntityToEvent ( OutboxEntity outboxEntity, OutboxSerializerHelper serializerHelper) {
        log.info("[infra.r2dbc.outbox.mapper] (fromEntityToEvent) payload=[ outboxEntity:{} ]", outboxEntity);

        LoanEvent loanEvent = serializerHelper.deserialize(outboxEntity);
        loanEvent.setId(outboxEntity.getId());

        log.info("[infra.r2dbc.outbox.mapper] (fromEntityToEvent) payload=[ event:{} ]", loanEvent);

        return loanEvent;
    }

}
