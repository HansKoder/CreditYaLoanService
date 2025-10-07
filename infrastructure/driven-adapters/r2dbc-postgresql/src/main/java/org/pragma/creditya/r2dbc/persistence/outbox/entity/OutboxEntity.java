package org.pragma.creditya.r2dbc.persistence.outbox.entity;


import io.r2dbc.postgresql.codec.Json;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.UUID;


@Table(name = "OUTBOX_EVENTS")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OutboxEntity {

    @Id
    @Column(value = "outbox_id")
    private UUID id;

    @Column(value = "aggregate_id")
    private String aggregateId;

    @Column(value = "aggregate_name")
    private String aggregateName;

    @Column(value = "event_type")
    private String eventType;

    @Enumerated(EnumType.STRING)
    @Column(value = "outbox_status")
    private OutboxStatus status;

    @Column(value = "payload")
    private Json payload;

    @Column("created_at")
    private Instant createdAt;

}

