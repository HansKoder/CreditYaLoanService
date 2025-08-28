package org.pragma.creditya.model.loan.gateways;

import reactor.core.publisher.Mono;

public interface UserClient {
    Mono<Boolean> exitByDocument(String document);
}
