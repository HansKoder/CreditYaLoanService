package org.pragma.creditya.r2dbc.persistence.outbox.repository;

import org.pragma.creditya.r2dbc.persistence.outbox.entity.OutboxEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import java.util.UUID;

public interface OutboxReactiveRepository extends R2dbcRepository<OutboxEntity, UUID> {
}
