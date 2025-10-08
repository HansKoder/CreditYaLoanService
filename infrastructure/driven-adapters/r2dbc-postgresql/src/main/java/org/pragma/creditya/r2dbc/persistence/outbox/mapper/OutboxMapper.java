package org.pragma.creditya.r2dbc.persistence.outbox.mapper;

import org.pragma.creditya.r2dbc.persistence.outbox.entity.OutboxEntity;
import org.pragma.creditya.r2dbc.persistence.outbox.entity.OutboxStatus;
import org.pragma.creditya.usecase.outbox.LoanOutboxMessage;
import org.pragma.creditya.usecase.outbox.OutboxTypeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class OutboxMapper {

    private final static Logger log = LoggerFactory.getLogger(OutboxMapper.class);

    public static OutboxEntity fromOutboxMessageToEntity (LoanOutboxMessage outboxMessage) {
        log.info("[infra.r2dbc.outbox.mapper] (fromOutboxMessageToEntity) Payload=[ outboxMessage:{} ]", outboxMessage);

        OutboxEntity entity = OutboxEntity.builder()
                .aggregateId(outboxMessage.getAggregateId().toString())
                .aggregateName(outboxMessage.getAggregateName())
                .eventType(outboxMessage.getType().name())
                .status(OutboxStatus.valueOf(outboxMessage.getStatus().name()))
                .build();

        // For handling update
        if (outboxMessage.getId() != null) entity.setId(outboxMessage.getId());

        log.info("[infra.r2dbc.outbox.mapper] (fromOutboxMessageToEntity) from outboxMessage to entity, payload: {}", entity);

        return entity;
    }

    public static LoanOutboxMessage fromOutboxEntityToOutboxMessage (OutboxEntity outboxEntity) {
        log.info("[infra.r2dbc.outbox.mapper] (fromOutboxEntityToOutboxMessage) Payload=[ outboxEntity:{} ]", outboxEntity);

        LoanOutboxMessage outboxMessage = LoanOutboxMessage.builder()
                .id(outboxEntity.getId())
                .aggregateId(UUID.fromString(outboxEntity.getAggregateId()))
                .aggregateName(outboxEntity.getAggregateName())
                .type(OutboxTypeEvent.valueOf(outboxEntity.getEventType()))
                .payload(outboxEntity.getPayload().asString())
                .build();

        log.info("[infra.r2dbc.outbox.mapper] (fromOutboxEntityToOutboxMessage) payload: {}", outboxMessage);

        return outboxMessage;
    }


}
