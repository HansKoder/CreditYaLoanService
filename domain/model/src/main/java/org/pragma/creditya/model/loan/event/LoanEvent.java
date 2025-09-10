package org.pragma.creditya.model.loan.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.shared.domain.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

@ToString
@Getter
@RequiredArgsConstructor
public abstract class LoanEvent implements DomainEvent<Loan> {
    private final UUID aggregateId;
    private final int version;
    private final Instant timestamp;
    private final String eventType;
    private final String aggregateType;
}
