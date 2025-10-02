package org.pragma.creditya.consumer.customer.adapter;

import org.pragma.creditya.model.customer.entity.Customer;
import org.pragma.creditya.model.customer.gateway.CustomerRepository;
import org.pragma.creditya.model.customer.valueobject.Document;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CustomerRestConsumerAdapter implements CustomerRepository {

    @Override
    public Mono<Boolean> verifyCustomerByDocument(Document document) {
        return null;
    }

    @Override
    public Mono<Customer> getCustomerByDocument(Document document) {
        return null;
    }

    @Override
    public Mono<Boolean> verifyCustomerByDocumentAndEmail(String document, String email) {
        return null;
    }
}
