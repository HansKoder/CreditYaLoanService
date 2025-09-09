package org.pragma.creditya.r2dbc.persistence.event.entity;

import io.r2dbc.postgresql.codec.Json;
import lombok.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.UUID;


@Table(name = "loan_events")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EventEntity {

    @Id
    @Column(value = "event_id")
    private UUID eventId;

    @Column(value = "aggregate_id")
    private UUID aggregateId;

    @Column(value = "aggregate_type")
    private String aggregateType;

    @Column(value = "event_type")
    private String eventType;

    @Column(value = "payload")
    private Json payload;

    @Column("created_at")
    private Instant createdAt;

}
