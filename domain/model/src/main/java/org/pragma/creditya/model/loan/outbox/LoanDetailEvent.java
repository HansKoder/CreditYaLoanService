package org.pragma.creditya.model.loan.outbox;

public interface LoanDetailEvent {
    String eventType();
    String aggregate();
    String aggregateId();
}
