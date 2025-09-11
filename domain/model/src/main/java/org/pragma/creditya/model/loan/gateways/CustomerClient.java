package org.pragma.creditya.model.loan.gateways;

import org.pragma.creditya.model.loan.entity.CustomerRead;
import reactor.core.publisher.Mono;

public interface CustomerClient {
    Mono<Boolean> exitByDocument(String document);
    Mono<CustomerRead> verifyOwnershipCustomer (String document, String email);
}
