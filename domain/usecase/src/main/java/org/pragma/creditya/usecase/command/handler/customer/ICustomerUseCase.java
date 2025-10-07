package org.pragma.creditya.usecase.command.handler.customer;

import org.pragma.creditya.model.customer.entity.Customer;
import org.pragma.creditya.model.customer.valueobject.Document;
import reactor.core.publisher.Mono;

public interface ICustomerUseCase {

    Mono<Void> verifyMyIdentity (Document document);
    Mono<Customer> getCustomer (Document document);

}
