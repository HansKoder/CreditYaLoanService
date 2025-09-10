package org.pragma.creditya.model.loan.bus;

import org.pragma.creditya.model.loan.event.LoanEvent;
import reactor.core.publisher.Flux;

public interface EventBus {
    void publish(LoanEvent event);
    <T extends LoanEvent> Flux<T> subscribe(Class<T> eventType);
}
