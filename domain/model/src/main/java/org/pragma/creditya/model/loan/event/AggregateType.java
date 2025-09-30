package org.pragma.creditya.model.loan.event;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.shared.domain.model.entity.AggregateRoot;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum AggregateType {
    AGGREGATE_LOAN(Loan.class);
    private final Class<? extends AggregateRoot<?>> aggregateClass;
}
