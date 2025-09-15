package org.pragma.creditya.consumer;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.pragma.creditya.consumer.exception.CustomerBadRequestException;
import org.pragma.creditya.consumer.exception.CustomerServiceException;
import org.pragma.creditya.consumer.exception.CustomerServiceUnavailableException;
import org.pragma.creditya.consumer.exception.CustomerUnauthorizedException;
import org.pragma.creditya.consumer.response.CustomerResponse;
import org.pragma.creditya.consumer.response.CustomerErrorResponse;
import org.pragma.creditya.model.loan.entity.CustomerRead;
import org.pragma.creditya.model.loan.gateways.CustomerClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RestConsumer implements CustomerClient{
    private final WebClient client;

    private final static Logger log = LoggerFactory.getLogger(RestConsumer.class);

    @CircuitBreaker(name = "testGet", fallbackMethod = "operationIsDown")
    public Mono<CustomerResponse> getVerifyOwnershipCustomer(String document, String email) {
        return Mono.deferContextual(ctx -> {
            String token = Optional.of(ctx.get("token"))
                    .map(Object::toString)
                    .orElse("");

            log.info("[infra.rest-consumer] (extract-token) token: {}", token);

            return client
                    .get()
                    // .uri("/api/v1/users/verify-ownership-customer")
                    .uri(uriBuilder -> uriBuilder

                            .queryParam("document", document)
                            .queryParam("email", email)
                            .build())
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .retrieve()
                    .onStatus(status -> status.value() == 400,
                            response -> response.bodyToMono(CustomerErrorResponse.class)
                                    .defaultIfEmpty(new CustomerErrorResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase()))
                                    .flatMap(err -> {
                                        log.error("[infra.rest-consumer] 400 BadRequest: {}", err);
                                        return Mono.error(new CustomerBadRequestException(err.error()));
                                    }))
                    .onStatus(status -> status.value() == 401,
                            response -> response.bodyToMono(CustomerErrorResponse.class)
                                    .defaultIfEmpty(new CustomerErrorResponse(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase()))
                                    .flatMap(err -> {
                                        log.error("[infra.rest-consumer] 403 Forbidden: {}", err);
                                        return Mono.error(new CustomerUnauthorizedException(err.error()));
                                    }))
                    .onStatus(HttpStatusCode::isError,
                            response -> response.bodyToMono(CustomerErrorResponse.class)
                                    .defaultIfEmpty(new CustomerErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()))
                                    .flatMap(err -> {
                                        log.error("[infra.rest-consumer] Remote error {}: {}", response.statusCode(), err);
                                        return Mono.error(new CustomerServiceException("Remote service error: " + err.error()));
                                    }))
                    .bodyToMono(CustomerResponse.class);
        });
    }

    // Fallback method (para CircuitBreaker)
    public Mono<CustomerResponse> operationIsDown(String document, Throwable ex) {
        log.error("[infra.rest-consumer] Fallback triggered for document: {}, error: {}", document, ex.getMessage());
        return Mono.error(new CustomerServiceUnavailableException("Customer service unavailable"));
    }

    @Deprecated
    @Override
    public Mono<Boolean> exitByDocument(String document) {
            log.info("[infra.rest-consumer] 1.0 make request to user-service, payload: [ document:{} ]", document);
            return Mono.just(Boolean.FALSE);
    }

    @Override
    public Mono<CustomerRead> verifyOwnershipCustomer(String document, String email) {
        log.info("[infra.rest-consumer] (verifyOwnershipCustomer), payload: [ document:{}, email:{} ]", document, email);
        return getVerifyOwnershipCustomer(document, email)
                .map(r -> CustomerRead.builder()
                        .document(r.document())
                        .baseSalary(r.baseSalary())
                        .name(r.name())
                        .email(r.email())
                        .build())
                .doOnSuccess(customer -> log.info("[infra.rest-consumer] (verifyOwnershipCustomer), success customer was identified, payload: [ customer:{} ]", customer))
                .doOnError(e -> log.info("[infra.rest-consumer] (verifyOwnershipCustomer), fail, unexpected error,  payload=[ error:{} ]", e.getMessage()));
    }

}
