package org.pragma.creditya.r2dbc.persistence.outbox.adapter;

import io.r2dbc.postgresql.codec.Json;
import lombok.RequiredArgsConstructor;
import org.pragma.creditya.r2dbc.persistence.outbox.entity.OutboxEntity;
import org.pragma.creditya.r2dbc.persistence.outbox.entity.OutboxStatus;
import org.pragma.creditya.r2dbc.persistence.outbox.helper.OutboxSerializerHelper;
import org.pragma.creditya.r2dbc.persistence.outbox.mapper.OutboxMapper;
import org.pragma.creditya.r2dbc.persistence.outbox.repository.OutboxReactiveRepository;
import org.pragma.creditya.usecase.outbox.LoanOutboxMessage;
import org.pragma.creditya.usecase.outbox.gateway.OutboxRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class OutboxAdapter implements OutboxRepository {

    private final OutboxReactiveRepository repository;
    private final OutboxSerializerHelper outboxSerializerHelper;
    private final Logger log = LoggerFactory.getLogger(OutboxAdapter.class);


    @Override
    public <T> Mono<Void> saveOutboxMessage(LoanOutboxMessage outboxMessage, T payload) {
        log.info("[infra.r2dbc.outbox] (saveOutboxMessage) (step-1) save event  payload=[ outboxMessage:{} ] ", outboxMessage);

        if (payload == null) {
            log.error("[infra.r2dbc.outbox] [ERROR] (saveOutboxMessage) (step-2) payload is null, payload must be mandatory");
            return Mono.empty();
        }

        log.info("[infra.r2dbc.outbox] (saveOutboxMessage) (step-2) payload after being checked  payload=[ payload:{} ] ", payload);

        return Mono.just(outboxMessage)
                .map(this::mapToOutboxEntity)
                // set payload
                .map(entity -> {
                    log.info("[infra.r2dbc.outbox] (saveOutboxMessage) (step-3) before being serialized payload payload=[ payload:{} ] ", entity.getPayload());
                    entity.setPayload(Json.of(outboxSerializerHelper.serializePayload(payload)));
                    log.info("[infra.r2dbc.outbox] (saveOutboxMessage) (step-4) after being serialized payload payload=[ payload:{} ] ", entity.getPayload());
                    return entity;
                })
                .doOnSuccess(entity -> log.info("[infra.r2dbc.outbox] (saveOutboxMessage) (step-3) mapped from outboxMessage to OutboxEntity, response=[ entity:{} ]", entity))
                .doOnError(err -> log.info("[infra.r2dbc.outbox] [ERROR] (saveOutboxMessage) (step-3) failed mapped, response=[ errorDetail:{} ]", err.getMessage()))
                .flatMap(e -> {
                    log.info("[infra.r2dbc.outbox] (save) this entity will be persisted {}", e);
                    return repository.save(e);
                })
                .doOnError(e -> log.info("[infra.r2dbc.outbox] [ERROR] (save) error when save event, payload: {}, detail: {}", e.getMessage(), e.getStackTrace()))
                .then();
    }

    @Override
    public Flux<LoanOutboxMessage> findByPending() {
        log.info("[infra.r2dbc.outbox] (findByPending) ");

        OutboxEntity entity = OutboxEntity.builder()
                .status(OutboxStatus.STARTED)
                .build();

        return repository.findAll(Example.of(entity))
                .map(OutboxMapper::fromOutboxEntityToOutboxMessage);
    }

    @Override
    public Mono<Void> markAsCompleted(UUID outboxId) {
        log.info("[infra.r2dbc.outbox] (markAsCompleted) (step-1) Payload=[ outboxId:{} ]", outboxId);

        return repository.findById(outboxId)
                .log()
                .doOnSuccess(res -> log.info("[infra.r2dbc.outbox] (markAsCompleted) (step-2) get find by id, response=[ outboxEntity:{} ]", res))
                .map(ent -> {
                    ent.setStatus(OutboxStatus.COMPLETED);
                    return ent;
                })
                .flatMap(repository::save)
                .doOnSuccess(res -> log.info("[infra.r2dbc.outbox] (markAsCompleted) (step-3) entity was persisted with successful response=[ outboxEntity:{} ]", res))
                .then();
    }

    @Override
    public Mono<Void> markAFailed(UUID outboxId) {
        log.info("[infra.r2dbc.outbox] (markAFailed) Payload=[ outboxId:{} ]", outboxId);

        return repository.findById(outboxId)
                .map(ent -> {
                    ent.setStatus(OutboxStatus.FAILED);
                    return ent;
                })
                .flatMap(repository::save)
                .then();
    }


    private OutboxEntity mapToOutboxEntity (LoanOutboxMessage outboxMessage) {
        log.info("[infra.r2dbc.outbox] (mapToOutboxEntity) (step-1) payload=[ outboxMessage:{} ] ", outboxMessage);

        OutboxEntity entity = OutboxMapper.fromOutboxMessageToEntity(outboxMessage);
        log.info("[infra.r2dbc.outbox] (mapToOutboxEntity) (step-2) mapped to outboxEntity response=[ outboxEntity:{} ] ", entity);

        return entity;
    }
}
