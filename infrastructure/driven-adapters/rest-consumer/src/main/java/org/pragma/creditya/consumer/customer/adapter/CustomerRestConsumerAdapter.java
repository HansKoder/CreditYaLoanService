package org.pragma.creditya.consumer.customer.adapter;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.consumer.customer.rest.RestConsumer;
import org.pragma.creditya.consumer.customer.rest.RestHelper;
import org.pragma.creditya.consumer.customer.rest.mapper.RestConsumerMapper;
import org.pragma.creditya.consumer.customer.rest.payload.GetCustomerPayload;
import org.pragma.creditya.consumer.customer.rest.payload.VerifyCustomerPayload;
import org.pragma.creditya.consumer.customer.rest.response.VerifyCustomerResponse;
import org.pragma.creditya.model.customer.entity.Customer;
import org.pragma.creditya.model.customer.gateway.CustomerRepository;
import org.pragma.creditya.model.customer.valueobject.Document;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomerRestConsumerAdapter implements CustomerRepository {

    private final RestConsumer restConsumer;

    @Override
    public Mono<Boolean> verifyCustomerByDocument(Document document) {
        return Mono.empty();
    }

    @Override
    public Mono<Customer> getCustomerByDocument(Document document) {
        return RestHelper.extractToken()
                .map(token -> new GetCustomerPayload(document.getValue(), token))
                .flatMap(restConsumer::getCustomer)
                .map(RestConsumerMapper::toEntity);
    }

    @Override
    public Mono<Boolean> verifyCustomerByDocumentAndEmail(Document document, String email) {
        return RestHelper.extractToken()
                .map(token -> new VerifyCustomerPayload(document.getValue(), email, token))
                .flatMap(restConsumer::verifyOwnership)
                .map(VerifyCustomerResponse::allowed);
    }
}
