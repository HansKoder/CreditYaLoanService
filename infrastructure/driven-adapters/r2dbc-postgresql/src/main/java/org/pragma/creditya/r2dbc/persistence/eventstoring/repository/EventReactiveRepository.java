package org.pragma.creditya.r2dbc.persistence.eventstoring.repository;

import org.pragma.creditya.r2dbc.persistence.eventstoring.entity.EventEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import java.util.UUID;

public interface EventReactiveRepository extends R2dbcRepository<EventEntity, UUID> {
}
