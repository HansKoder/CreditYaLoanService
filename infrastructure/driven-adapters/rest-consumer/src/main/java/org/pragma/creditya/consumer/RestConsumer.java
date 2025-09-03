package org.pragma.creditya.consumer;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.pragma.creditya.model.loan.gateways.CustomerClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RestConsumer implements CustomerClient{
    private final WebClient client;

    private final static Logger log = LoggerFactory.getLogger(RestConsumer.class);

    @CircuitBreaker(name = "testGet", fallbackMethod = "testGetOk")
    public Mono<ObjectResponse> customerExistByDocument(String document) {

            return client
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("document", document).build())
                    .retrieve()
                    .bodyToMono(ObjectResponse.class);

    }

    public Mono<String> testGetOk(Exception ignored) {
        return Mono.error(new InfrastructureException("Error Customer Service, detail : " + ignored.getMessage()));
    }


    @Override
    public Mono<Boolean> exitByDocument(String document) {
        return Mono.deferContextual(ctx -> {
            String correlationId = ctx.get("X-Correlation-Id");
            log.info("correlationId[{}] [infra.rest-consumer] 1.0 make request to user-service, payload: [ document:{} ]", correlationId, document);
            return customerExistByDocument(document)
                    .doOnSuccess(response -> log.info("correlationId[{}] [infra.rest-consumer] 1.1 request was consumed, payload: [ response:{} ]", correlationId, response))
                    .map(ObjectResponse::getExists);

        });
    }
}
