package org.pragma.creditya.model.loan.gateways;

import reactor.core.publisher.Mono;

public interface SQSProducer {
    Mono<Void> sendMessage (Object payload);
    Mono<Void> sendMessage (String payload);
}
