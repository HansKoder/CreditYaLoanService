package org.pragma.creditya.consumer;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.pragma.creditya.model.loan.gateways.CustomerClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RestConsumer implements CustomerClient{
    private final WebClient client;

    private final static Logger log = LoggerFactory.getLogger(RestConsumer.class);

    @CircuitBreaker(name = "testGet", fallbackMethod = "operationIsDown")
    public Mono<ObjectResponse> customerExistByDocument(String document) {

            return client
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("document", document).build())
                    .retrieve()
                    .onStatus(HttpStatusCode::isError,
                            response -> Mono.error(new RuntimeException("Remote service error: " + response.statusCode())))
                    .bodyToMono(ObjectResponse.class);

    }

    public Mono<ObjectResponse> operationIsDown(String document) {
        return Mono.just(new ObjectResponse(false));
    }


    @Override
    public Mono<Boolean> exitByDocument(String document) {
            log.info("[infra.rest-consumer] 1.0 make request to user-service, payload: [ document:{} ]", document);
            return customerExistByDocument(document)
                    .doOnSuccess(response -> log.info("[infra.rest-consumer] 1.1 request was consumed, payload: [ response:{} ]", response))
                    .map(ObjectResponse::getExists);
                    //.onErrorResume(err -> Mono.error(new InfrastructureException("Customer Service is not working, error: " + err.getMessage())));

    }
}
