package org.pragma.creditya.model.loan.gateways;

import org.pragma.creditya.model.loan.entity.CustomerRead;
import reactor.core.publisher.Mono;

public interface CustomerClient {
    Mono<Boolean> exitByDocument(String document);
    Mono<Boolean> verifyOwnershipCustomer (String document, String email);
    Mono<CustomerRead> getCustomerByDocument (String document);
}
