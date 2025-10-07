package org.pragma.creditya.r2dbc.persistence.eventstoring.repository;

import org.pragma.creditya.r2dbc.persistence.eventstoring.entity.EventEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface EventReactiveRepository extends R2dbcRepository<EventEntity, UUID> {
    Flux<EventEntity> findByAggregateId(UUID aggregateId);
}
