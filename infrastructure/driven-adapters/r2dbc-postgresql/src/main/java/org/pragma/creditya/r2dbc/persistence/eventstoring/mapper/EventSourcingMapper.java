package org.pragma.creditya.r2dbc.persistence.eventstoring.mapper;

import io.r2dbc.postgresql.codec.Json;
import lombok.RequiredArgsConstructor;
import org.pragma.creditya.model.loan.event.EventType;
import org.pragma.creditya.model.loan.event.LoanEvent;
import org.pragma.creditya.r2dbc.persistence.eventstoring.entity.EventEntity;
import org.pragma.creditya.r2dbc.persistence.eventstoring.helper.EventSerializerHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class EventSourcingMapper {

    private final EventSerializerHelper eventSerializerHelper;

    private final Logger log = LoggerFactory.getLogger(EventSourcingMapper.class);

    public EventEntity toData (LoanEvent event) {
        log.info("[infra.r2dbc.event.sourcing] (mapper) toData: {}", event);
        UUID aggregateId = event.getAggregateId() == null ? null : event.getAggregateId();

        EventEntity entity = EventEntity.builder()
                .aggregateId(aggregateId)
                .aggregateType(event.getAggregateType().name())
                .eventType(event.getEventType().getEventClass().getSimpleName())
                .payload(Json.of(eventSerializerHelper.serialize(event)))
                .build();

        log.info("[infra.r2dbc] (mapper) from event to data, payload: {}", entity);

        return entity;
    }

    public LoanEvent toEntity (EventEntity data) {
        log.info("[infra.r2dbc.event.sourcing] (mapper) toEntity, payload=[ data:{} ]", data);

        EventType eventType = EventType
                .fromClass(eventSerializerHelper.resolveEventClass(data.getEventType()));

        LoanEvent entity = LoanEvent.builder()
                .id(data.getEventId())
                .aggregateId(data.getAggregateId())
                .eventType(eventType)
                .payload(eventSerializerHelper.deserialize(data))
                .build();

        log.info("[infra.r2dbc.event.sourcing] (mapper) from data to entity, response=[ entity:{} ]", entity);

        return entity;
    }

}
