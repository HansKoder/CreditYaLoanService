package org.pragma.creditya.consumer.customer;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.consumer.cons.RestConstant;
import org.pragma.creditya.consumer.customer.payload.GetCustomerPayload;
import org.pragma.creditya.consumer.customer.payload.VerifyCustomerPayload;
import org.pragma.creditya.consumer.exception.*;
import org.pragma.creditya.consumer.customer.response.CustomerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RestConsumer {

    private final WebClient userWebClient;

    private final static Logger log = LoggerFactory.getLogger(RestConsumer.class);

    public Mono<Boolean> verifyOwnership(VerifyCustomerPayload payload) {
        return userWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(RestConstant.CUSTOMER_ENDPOINT_VERIFY)
                        .queryParam(RestConstant.CUSTOMER_PARAM_DOCUMENT, payload.document())
                        .queryParam(RestConstant.CUSTOMER_PARAM_EMAIL, payload.email())
                        .build())
                .header(HttpHeaders.AUTHORIZATION, RestConstant.BEARER + payload.token())
                .retrieve()
                .onStatus(HttpStatusCode::isError, RestHelper::handleErrors)
                .toBodilessEntity()
                .map(response -> response.getStatusCode().is2xxSuccessful());
    }

    public Mono<CustomerResponse> getCustomer(GetCustomerPayload payload) {
        log.info("[infra.rest-consumer] (getConsumer) (step-0) payload=[ doc:{} token:{} ]", payload.document(), payload.token());
        return userWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(RestConstant.CUSTOMER_ENDPOINT_GET_DOCUMENT)
                        .queryParam(RestConstant.CUSTOMER_PARAM_DOCUMENT, payload.document())
                        .build())
                .header(HttpHeaders.AUTHORIZATION, RestConstant.BEARER + payload.token())
                .retrieve()
                .onStatus(HttpStatusCode::isError, RestHelper::handleErrors)
                .bodyToMono(CustomerResponse.class)
                .doOnSuccess(e -> log.info("[infra.rest-consumer] (getCustomer) (step-1) success, get customer: response=[ customer:{} ]", e))
                .doOnError(e -> log.info("[infra.rest-consumer] (getCustomer) (step-1) error, get customer: response=[ error:{} ]", e.getMessage()));
    }

    /*
    public Mono<Boolean> exitByDocument(String document) {
        return Mono.just(Boolean.FALSE);
    }

    public Mono<Boolean> verifyOwnershipCustomer(String document, String email) {
        return RestHelper.extractToken()
                .flatMap(token -> verifyOwnership(document, email, token));
    }

    public Mono<Customer> getCustomerByDocument(String document) {
        log.info("[infra.rest-consumer] (getCustomerByDocument) payload=[ document:{} ]", document);
        return RestHelper.extractToken()
                .flatMap(token -> getCustomer(document, token))
                .map(RestConsumerMapper::toEntity);
    }

     */
}
