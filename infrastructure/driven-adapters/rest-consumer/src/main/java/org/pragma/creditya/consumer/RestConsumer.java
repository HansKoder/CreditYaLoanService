package org.pragma.creditya.consumer;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.consumer.exception.*;
import org.pragma.creditya.consumer.mapper.RestConsumerMapper;
import org.pragma.creditya.consumer.response.GetCustomerByDocumentResponse;
import org.pragma.creditya.consumer.response.CustomerErrorResponse;
import org.pragma.creditya.model.loan.entity.CustomerRead;
import org.pragma.creditya.model.loan.gateways.CustomerClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
@RequiredArgsConstructor
public class RestConsumer implements CustomerClient{
    private final WebClient userWebClient;

    private final static Logger log = LoggerFactory.getLogger(RestConsumer.class);

    public Mono<Boolean> verifyOwnership(String document, String email, String token) {
        return userWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/users/verify-ownership-customer")
                        .queryParam("document", document)
                        .queryParam("email", email)
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .onStatus(HttpStatusCode::isError, this::handleErrors)
                .toBodilessEntity()
                .map(response -> response.getStatusCode().is2xxSuccessful());
    }

    public Mono<GetCustomerByDocumentResponse> getCustomer(String document, String token) {
        log.info("[infra.rest-consumer] (getConsumer) (step-0) payload=[ doc:{} token:{} ]", document, token);
        return userWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/users/document")
                        .queryParam("document", document)
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .onStatus(HttpStatusCode::isError, this::handleErrors)
                .bodyToMono(GetCustomerByDocumentResponse.class)
                .doOnSuccess(e -> log.info("[infra.rest-consumer] (getCustomer) (step-1) success, get customer: response=[ customer:{} ]", e))
                .doOnError(e -> log.info("[infra.rest-consumer] (getCustomer) (step-1) error, get customer: response=[ error:{} ]", e.getMessage()));
    }

    private Mono<? extends Throwable> handleErrors(ClientResponse response) {
        return response.bodyToMono(CustomerErrorResponse.class)
                .defaultIfEmpty(new CustomerErrorResponse(
                        response.statusCode().value(),
                        "Error unexpected"
                ))
                .flatMap(err -> {
                    log.error("[infra.rest-consumer] Remote error {}: {}", response.statusCode(), err);

                    return switch (response.statusCode()) {
                        case BAD_REQUEST -> Mono.error(new CustomerBadRequestException(err.message()));
                        case UNAUTHORIZED -> Mono.error(new CustomerUnauthorizedException(err.message()));
                        default -> Mono.error(new CustomerServiceException("Remote service error: " + err.message()));
                    };
                });
    }

    private Mono<String> extractToken () {
        log.info("[infra.rest-consumer] (extractToken)");
        return Mono.deferContextual(ctx -> {
            String token = Optional.of(ctx.get("token"))
                    .map(Object::toString)
                    .orElse("");

            log.info("[infra.rest-consumer] (extract-token) token was extracted: {}", token);

            return Mono.just(token)
                    .switchIfEmpty(Mono.error(new TokenIsMissingException("Token is missing")));
        });
    }

    @Override
    public Mono<Boolean> exitByDocument(String document) {
        return Mono.just(Boolean.FALSE);
    }

    @Override
    public Mono<Boolean> verifyOwnershipCustomer(String document, String email) {
        return extractToken()
                .flatMap(token -> verifyOwnership(document, email, token));
    }

    @Override
    public Mono<CustomerRead> getCustomerByDocument(String document) {
        log.info("[infra.rest-consumer] (getCustomerByDocument) payload=[ document:{} ]", document);
        return extractToken()
                .flatMap(token -> getCustomer(document, token))
                .map(RestConsumerMapper::toCustomerRead);
    }
}
