package org.pragma.creditya.r2dbc.persistence.outbox.adapter;

import io.r2dbc.postgresql.codec.Json;
import lombok.RequiredArgsConstructor;
import org.pragma.creditya.model.loan.event.LoanEvent;
import org.pragma.creditya.model.loan.gateways.OutboxRepository;
import org.pragma.creditya.r2dbc.persistence.outbox.entity.OutboxEntity;
import org.pragma.creditya.r2dbc.persistence.outbox.entity.OutboxStatus;
import org.pragma.creditya.r2dbc.persistence.outbox.helper.OutboxSerializerHelper;
import org.pragma.creditya.r2dbc.persistence.outbox.mapper.OutboxMapper;
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
                .map(outboxEntity -> OutboxMapper.fromEntityToEvent(outboxEntity, outboxSerializerHelper))
                .log();
    }

    @Override
    public Mono<Void> markAsCompleted(UUID outboxId) {
        log.info("[infra.r2dbc.outbox] (markAsCompleted) payload=[outboxId:{}]", outboxId);

        if (outboxId == null) {
            log.warn("[infra.r2dbc.outbox] (markAsCompleted) OutboxId is null, should be checked");
            return Mono.empty();
        }

        return repository.findById(outboxId)
                .map(data -> {
                    data.setStatus(OutboxStatus.COMPLETED);
                    return data;
                })
                .flatMap(repository::save)
                // .map(outboxEntity -> OutboxMapper.fromEntityToEvent(outboxEntity, outboxSerializerHelper))
                // .flatMap(e -> saveEvent(e, OutboxStatus.COMPLETED))
                .doOnSuccess(outboxEntity -> log.info("[infra.r2dbc.outbox] (markAsCompleted) was marked completed with successful, payload=[entity:{}]", outboxEntity))
                .doOnError(err -> log.error("[infra.r2dbc.outbox] (markAsCompleted) had failed, detail error={}", err.getMessage()))
                .then();
    }

    @Override
    public Mono<Void> markAFailed(UUID outboxId) {
        log.info("[infra.r2dbc.outbox] (markAFailed) payload=[outboxId:{}]", outboxId);

        if (outboxId == null) {
            log.warn("[infra.r2dbc.outbox] (markAFailed) OutboxId is null, should be checked");
            return Mono.empty();
        }

        return repository.findById(outboxId)
                .map(data -> {
                    data.setStatus(OutboxStatus.FAILED);
                    return data;
                })
                .flatMap(repository::save)
                // .map(outboxEntity -> OutboxMapper.fromEntityToEvent(outboxEntity, outboxSerializerHelper))
                // .flatMap(e -> saveEvent(e, OutboxStatus.FAILED))

                .doOnSuccess(outboxEntity -> log.info("[infra.r2dbc.outbox] (markAFailed) was marked failed with successful, payload=[entity:{}]", outboxEntity))
                .doOnError(err -> log.error("[infra.r2dbc.outbox] (markAFailed) had failed, detail error={}", err.getMessage()))
                .then();
    }

    @Override
    public Mono<Void> saveAll(List<LoanEvent> events) {
        log.info("[infra.r2dbc.outbox] (saveAll) payload [ size:{}, events:{} ]", events.size(), events);
        return Flux.fromIterable(events)
                .flatMap(e -> saveEvent(e, OutboxStatus.STARTED))
                .doOnError(e -> log.info("[infra.r2dbc.outbox] (save-all) error when persisting all events, payload: {}", e.getMessage()))
                .then();
    }

    private Mono<Void> saveEvent(LoanEvent event, OutboxStatus status) {
        log.info("[infra.r2dbc.outbox] (event) save event  payload: {}", event);
        return Mono.fromCallable(() -> OutboxMapper.fromEventToEntity(event, outboxSerializerHelper, status))
                .flatMap(e -> {
                    log.info("[infra.r2dbc.outbox] (save) this entity will be persisted {}", e);
                    return repository.save(e);
                })
                .doOnError(e -> log.info("[infra.r2dbc.outbox] (save) error when save event, payload: {}, detail: {}", e.getMessage(), e.getStackTrace()))
                .then();
    }

}
