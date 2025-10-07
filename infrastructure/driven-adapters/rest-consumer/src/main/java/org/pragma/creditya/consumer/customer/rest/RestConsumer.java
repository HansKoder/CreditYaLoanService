package org.pragma.creditya.consumer.customer.rest;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.consumer.InfrastructureException;
import org.pragma.creditya.consumer.customer.cons.CustomerRestConstant;
import org.pragma.creditya.consumer.customer.rest.payload.GetCustomerPayload;
import org.pragma.creditya.consumer.customer.rest.payload.VerifyCustomerPayload;
import org.pragma.creditya.consumer.customer.rest.response.VerifyCustomerResponse;
import org.pragma.creditya.consumer.customer.rest.response.CustomerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class RestConsumer {

    @Qualifier("CustomerGetWebClient")
    private final WebClient userWebClient;

    private final static Logger log = LoggerFactory.getLogger(RestConsumer.class);

    public RestConsumer(@Qualifier("CustomerGetWebClient") WebClient userWebClient) {
        this.userWebClient = userWebClient;
    }

    public Mono<VerifyCustomerResponse> verifyOwnership(VerifyCustomerPayload payload) {
        log.info("[infra.rest-consumer] (verify-customer) (step-0) payload=[ doc:{} email:{} token:{} ]",
                payload.document(),
                payload.token(),
                payload.token());

        return userWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(CustomerRestConstant.CUSTOMER_ENDPOINT_VERIFY)
                        .queryParam(CustomerRestConstant.CUSTOMER_PARAM_DOCUMENT, payload.document())
                        .queryParam(CustomerRestConstant.CUSTOMER_PARAM_EMAIL, payload.email())
                        .build())
                .header(HttpHeaders.AUTHORIZATION, CustomerRestConstant.BEARER + payload.token())
                .retrieve()
                // .onStatus(HttpStatusCode::isError, RestHelper::handleErrors)
                .bodyToMono(VerifyCustomerResponse.class)
                .doOnSuccess(e -> log.info("[infra.rest-consumer] (verify-customer) (step-1) success, verify: response=[ response:{} ]", e))
                .doOnError(e -> log.info("[infra.rest-consumer] (verify-customer) (step-1) error, verify: response=[ error:{} ]", e.getMessage()))
                .onErrorResume(err -> Mono.error(new InfrastructureException(err.getMessage())));
    }

    public Mono<CustomerResponse> getCustomer(GetCustomerPayload payload) {
        log.info("[infra.rest-consumer] (getConsumer) (step-0) payload=[ doc:{} token:{} ]", payload.document(), payload.token());
        return userWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(CustomerRestConstant.CUSTOMER_ENDPOINT_GET_DOCUMENT)
                        .queryParam(CustomerRestConstant.CUSTOMER_PARAM_DOCUMENT, payload.document())
                        .build())
                .header(HttpHeaders.AUTHORIZATION, CustomerRestConstant.BEARER + payload.token())
                .retrieve()
                // .onStatus(HttpStatusCode::isError, RestHelper::handleErrors)
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
