package org.pragma.creditya.asynceventbus.projection.strategy;

import org.pragma.creditya.model.loan.event.LoanEvent;

public interface ProjectionStrategy {
    Boolean shouldBeExecuted(LoanEvent event);
    void replicate (LoanEvent event);
}
