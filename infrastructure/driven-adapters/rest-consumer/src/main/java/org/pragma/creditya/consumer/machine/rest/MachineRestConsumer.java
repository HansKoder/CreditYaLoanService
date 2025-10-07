package org.pragma.creditya.consumer.machine.rest;

import org.pragma.creditya.consumer.InfrastructureException;
import org.pragma.creditya.consumer.machine.cons.MachineRestConstant;
import org.pragma.creditya.consumer.machine.rest.payload.AuthenticationMachinePayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class MachineRestConsumer {

    @Qualifier("MachineGetWebClient")
    private final WebClient webClient;

    private final static Logger log = LoggerFactory.getLogger(MachineRestConsumer.class);

    public MachineRestConsumer(@Qualifier("MachineGetWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<String> authenticationMachine(AuthenticationMachinePayload payload) {
        log.info("[infra.rest-consumer.machine] (authentication) (step-0) payload=[ clientId:{}]",
                payload.clientId());

        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(MachineRestConstant.MACHINE_ENDPOINT_AUTHENTICATION)
                        .build())
                .bodyValue(payload)
                .exchangeToMono(response -> {
                    if (response.statusCode().isError()) {
                        log.error("[infra.rest-consumer.machine] (authentication) error status=[{}]", response.statusCode());
                        return Mono.error(new InfrastructureException("Error.."));
                    }

                    String token = response.headers()
                            .header(HttpHeaders.AUTHORIZATION)
                            .stream()
                            .findFirst()
                            .map(header -> header.replace(MachineRestConstant.BEARER, "").trim())
                            .orElse("");

                    if (token.isEmpty()) {
                        log.warn("[infra.rest-consumer.machine] (authentication) Token not found in response headers");
                        return Mono.error(new InfrastructureException("Token not found in response headers"));
                    }

                    log.info("[infra.rest-consumer.machine] (authentication) Token successfully obtained");
                    return Mono.just(token);
                })
                .doOnError(e -> log.error("[infra.rest-consumer.machine] (authentication) error: {}", e.getMessage()))
                .onErrorResume(err -> Mono.error(new InfrastructureException(err.getMessage())));
    }

}
