package org.pragma.creditya.model.loan.event;

import lombok.*;
import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loan.event.payload.LoanEventPayload;
import org.pragma.creditya.model.shared.domain.event.DomainEvent;

import java.util.UUID;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class LoanEvent implements DomainEvent<Loan> {
    private UUID id;
    private UUID aggregateId;
    private EventType eventType;
    private AggregateType aggregateType;
    private LoanEventPayload payload;
}
