package org.pragma.creditya.model.customer.gateway;

import org.pragma.creditya.model.customer.entity.Customer;
import org.pragma.creditya.model.customer.valueobject.Document;
import reactor.core.publisher.Mono;

public interface CustomerRepository {
    Mono<Boolean> verifyCustomerByDocument(Document document);
    Mono<Customer> getCustomerByDocument (Document document);
    Mono<Boolean> verifyCustomerByDocumentAndEmail (Document document, String email);

}
