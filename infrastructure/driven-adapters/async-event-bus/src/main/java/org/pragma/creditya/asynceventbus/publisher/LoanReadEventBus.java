package org.pragma.creditya.asynceventbus.publisher;

import org.pragma.creditya.model.loan.bus.EventBus;
import org.pragma.creditya.model.loan.event.LoanEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
public class LoanReadEventBus implements EventBus {

    private final Sinks.Many<LoanEvent> sink;

    public LoanReadEventBus() {
        this.sink = Sinks.many().multicast().onBackpressureBuffer();
    }

    @Override
    public void publish(LoanEvent event) {
        System.out.println("[infra.bus] (publish) register a new event, payload: " + event);
        sink.tryEmitNext(event);
    }

    @Override
    public <T extends LoanEvent> Flux<T> subscribe(Class<T> eventType) {
        System.out.println("[infra.bus] (subscribe) is assigned ");
        return sink.asFlux()
                .filter(event -> eventType.isAssignableFrom(event.getClass()))
                .map(event -> (T) event)
                .doOnSubscribe(s -> System.out.println("[bus] Subscribed to " + eventType.getSimpleName()));
    }

}
