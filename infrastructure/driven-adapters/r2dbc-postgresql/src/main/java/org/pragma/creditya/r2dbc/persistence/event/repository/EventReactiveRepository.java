package org.pragma.creditya.r2dbc.persistence.event.repository;

import org.pragma.creditya.r2dbc.persistence.event.entity.EventEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface EventReactiveRepository extends R2dbcRepository<EventEntity, UUID> {
}
