package org.pragma.creditya.model.loan.event;

import lombok.*;
import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loan.valueobject.LoanStatus;
import org.pragma.creditya.model.shared.domain.event.DomainEvent;

import java.util.UUID;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
public abstract class LoanEvent implements DomainEvent<Loan> {
    private UUID id;
    private UUID aggregateId;
    private EventType eventType;
    private AggregateType aggregateType;
}
