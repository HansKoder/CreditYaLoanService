package org.pragma.creditya.r2dbc.persistence.eventstoring.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.r2dbc.postgresql.codec.Json;
import lombok.RequiredArgsConstructor;
import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loan.event.LoanApplicationSubmittedEvent;
import org.pragma.creditya.model.loan.event.LoanEvent;
import org.pragma.creditya.model.loan.event.LoanResolutionApprovedEvent;
import org.pragma.creditya.model.loan.event.LoanResolutionRejectedEvent;
import org.pragma.creditya.model.loan.gateways.EventStoreRepository;
import org.pragma.creditya.r2dbc.persistence.eventstoring.entity.EventEntity;
import org.pragma.creditya.r2dbc.persistence.eventstoring.repository.EventReactiveRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class EventRepositoryAdapter implements EventStoreRepository {

    private final EventReactiveRepository repository;
    private final ObjectMapper objectMapper;

    private final Logger log = LoggerFactory.getLogger(EventRepositoryAdapter.class);


    @Override
    public Mono<Loan> findByAggregateIdLast(UUID aggregateId) {
        return null;
    }

    @Override
    public Flux<LoanEvent> findByAggregateId(UUID aggregateId) {
        log.info("[infra.r2dbc.event] (findByAggregateId) payload=[ aggregateId:{} ]", aggregateId.toString());
        return repository.findByAggregateId(aggregateId)
                .map(this::deserialize);
    }


    @Override
    public Mono<Void> saveEvent(LoanApplicationSubmittedEvent event) {
        log.info("[infra.r2dbc] (event) save event payload: {}", event);
        return Mono.fromCallable(() -> this.mapToPersist(event))
                .flatMap(repository::save)
                .then();
    }

    @Override
    public Mono<Void> saveAll(List<LoanEvent> events) {
        return Flux.fromIterable(events)
                .flatMap(this::saveEvent)
                .doOnError(e -> log.info("[infra.r2dbc] (save-all) error when persisting all events, payload: {}", e.getMessage()))
                .then();
    }


    private Mono<Void> saveEvent(LoanEvent event) {
        log.info("[infra.r2dbc] (event) save event  payload: {}", event);
        return Mono.fromCallable(() -> this.mapToPersist(event))
                .flatMap(e -> {
                    log.info("[infra.r2dbc] (save) this entity will be persisted {}", e);
                    return repository.save(e);
                })
                .doOnError(e -> log.info("[infra.r2dbc] (save) error when save event, payload: {}, detail: {}", e.getMessage(), e.getStackTrace()))
                .then();
    }

    private EventEntity mapToPersist (LoanEvent event) {
        log.info("[infra.r2dbc] (mapper) map to persist: {}", event);
        UUID aggregateId = event.getAggregateId() == null ? null : event.getAggregateId();

        EventEntity entity = EventEntity.builder()
                .aggregateId(aggregateId)
                .aggregateType(event.getAggregateType())
                .eventType(event.getEventType())
                .payload(Json.of(serialize(event)))
                .build();

        log.info("[infra.r2dbc] (mapper) from event to entity, payload: {}", entity);

        return entity;
    }

    private String serialize(LoanEvent event) {
        log.info("[infra.r2dbc] (object-mapper) serialize: {}", event);
        try {
            String payload = objectMapper.writeValueAsString(event);
            log.info("[infra.r2dbc] (object-mapper) serialized this is the payload: {}", payload);
            return payload;
        } catch (Exception e) {
            throw new RuntimeException("Error serializing event", e);
        }
    }

    private LoanEvent deserialize(EventEntity entity) {
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
            case "LoanResolutionApprovedEvent" -> LoanResolutionApprovedEvent.class;
            case "LoanResolutionRejectedEvent" -> LoanResolutionRejectedEvent.class;
            default -> throw new IllegalArgumentException("Unknown eventType: " + eventType);
        };
    }

}
