package org.pragma.creditya.model.loan.event;

import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.shared.domain.event.DomainEvent;
import org.pragma.creditya.model.shared.domain.model.entity.AggregateRoot;

public enum EventType {

    LOAN_SUBMITTED(LoanApplicationSubmittedEvent.class);

}
