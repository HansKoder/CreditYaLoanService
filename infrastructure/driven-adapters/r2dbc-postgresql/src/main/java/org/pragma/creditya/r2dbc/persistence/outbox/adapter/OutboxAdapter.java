package org.pragma.creditya.r2dbc.persistence.outbox.adapter;

import io.r2dbc.postgresql.codec.Json;
import lombok.RequiredArgsConstructor;
import org.pragma.creditya.model.loan.event.LoanEvent;
import org.pragma.creditya.model.loan.gateways.OutboxRepository;
import org.pragma.creditya.r2dbc.persistence.outbox.entity.OutboxEntity;
import org.pragma.creditya.r2dbc.persistence.outbox.entity.OutboxStatus;
import org.pragma.creditya.r2dbc.persistence.outbox.helper.OutboxSerializerHelper;
import org.pragma.creditya.r2dbc.persistence.outbox.repository.OutboxReactiveRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class OutboxAdapter implements OutboxRepository {

    private final OutboxReactiveRepository repository;
    private final OutboxSerializerHelper outboxSerializerHelper;
    private final Logger log = LoggerFactory.getLogger(OutboxAdapter.class);

    @Override
    public Flux<LoanEvent> findByPending() {
        log.info("[infra.r2dbc.outbox] (findByPending) ");
        OutboxEntity probe = OutboxEntity.builder()
                .status(OutboxStatus.STARTED)
                .build();

        return repository.findAll(Example.of(probe))
                .map(outboxSerializerHelper::deserialize)
                .log();
    }

    @Override
    public Mono<Void> markAsSent(UUID aggregateId) {
        return null;
    }

    @Override
    public Mono<Void> saveAll(List<LoanEvent> events) {
        log.info("[infra.r2dbc.outbox] (saveAll) payload [ size:{}, events:{} ]", events.size(), events);
        return Flux.fromIterable(events)
                .flatMap(this::saveEvent)
                .doOnError(e -> log.info("[infra.r2dbc.outbox] (save-all) error when persisting all events, payload: {}", e.getMessage()))
                .then();
    }

    private Mono<Void> saveEvent(LoanEvent event) {
        log.info("[infra.r2dbc.outbox] (event) save event  payload: {}", event);
        return Mono.fromCallable(() -> this.mapToPersist(event))
                .flatMap(e -> {
                    log.info("[infra.r2dbc.outbox] (save) this entity will be persisted {}", e);
                    return repository.save(e);
                })
                .doOnError(e -> log.info("[infra.r2dbc.outbox] (save) error when save event, payload: {}, detail: {}", e.getMessage(), e.getStackTrace()))
                .then();
    }

    private OutboxEntity mapToPersist (LoanEvent event) {
        log.info("[infra.r2dbc.outbox] (mapper) map to persist: {}", event);

        OutboxEntity entity = OutboxEntity.builder()
                .aggregateId(event.getAggregateId().toString())
                .aggregateName(event.getAggregateType())
                .eventType(event.getEventType())
                .status(OutboxStatus.STARTED)
                .payload(Json.of(outboxSerializerHelper.serialize(event)))
                .build();

        log.info("[infra.r2dbc.outbox] (mapper) from event to entity, payload: {}", entity);

        return entity;
    }
}
