package org.pragma.creditya.consumer;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.pragma.creditya.model.loan.gateways.CustomerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RestConsumer implements CustomerClient/* implements Gateway from domain */{
    private final WebClient client;


    // these methods are an example that illustrates the implementation of WebClient.
    // You should use the methods that you implement from the Gateway from the domain.
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

    /*

    @CircuitBreaker(name = "testPost")
    public Mono<ObjectResponse> testPost() {
        ObjectRequest request = ObjectRequest.builder()
            .val1("exampleval1")
            .val2("exampleval2")
            .build();
        return client
                .post()
                .body(Mono.just(request), ObjectRequest.class)
                .retrieve()
                .bodyToMono(ObjectResponse.class);
    }

     */

    @Override
    public Mono<Boolean> exitByDocument(String document) {
        return customerExistByDocument(document)
                .map(ObjectResponse::getExists);
    }
}
