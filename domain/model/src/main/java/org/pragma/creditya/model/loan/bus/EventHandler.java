package org.pragma.creditya.model.loan.bus;

import org.pragma.creditya.model.loan.event.LoanEvent;

public interface EventHandler<T extends LoanEvent> {
    void onEvent(T event);
}
