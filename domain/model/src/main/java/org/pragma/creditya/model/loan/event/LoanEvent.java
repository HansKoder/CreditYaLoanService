package org.pragma.creditya.model.loan.event;

import lombok.*;
import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loan.bus.EventBus;
import org.pragma.creditya.model.shared.domain.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public abstract class LoanEvent implements DomainEvent<Loan> {
    private UUID id;
    private UUID aggregateId;
    private int version;
    private Instant timestamp;
    private String eventType;
    private String aggregateType;
}
