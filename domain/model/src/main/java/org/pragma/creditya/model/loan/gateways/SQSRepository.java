package org.pragma.creditya.model.loan.gateways;

import reactor.core.publisher.Mono;

public interface SQSRepository {
    Mono<Void> sendMessage (Object payload);
}
