package org.pragma.creditya.model.loan.bus;

import org.pragma.creditya.model.loan.event.LoanEvent;

public interface EventHandler {
    void onEvent(LoanEvent event);
}
