package org.pragma.creditya.consumer.customer;

import org.pragma.creditya.consumer.customer.response.CustomerErrorResponse;
import org.pragma.creditya.consumer.exception.CustomerBadRequestException;
import org.pragma.creditya.consumer.exception.CustomerServiceException;
import org.pragma.creditya.consumer.exception.CustomerUnauthorizedException;
import org.pragma.creditya.consumer.exception.TokenIsMissingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class RestHelper {

    private final static Logger LOG = LoggerFactory.getLogger(RestHelper.class);

    public static Mono<? extends Throwable> handleErrors(ClientResponse response) {
        return response.bodyToMono(CustomerErrorResponse.class)
                .defaultIfEmpty(new CustomerErrorResponse(
                        response.statusCode().value(),
                        "Error unexpected"
                ))
                .flatMap(err -> {
                    LOG.error("[infra.rest-consumer] Remote error {}: {}", response.statusCode(), err);

                    return switch (response.statusCode()) {
                        case BAD_REQUEST -> Mono.error(new CustomerBadRequestException(err.message()));
                        case UNAUTHORIZED -> Mono.error(new CustomerUnauthorizedException(err.message()));
                        default -> Mono.error(new CustomerServiceException("Remote service error: " + err.message()));
                    };
                });
    }

    public static Mono<String> extractToken () {
        LOG.info("[infra.rest-consumer] (extractToken)");
        return Mono.deferContextual(ctx -> {
            String token = Optional.of(ctx.get("token"))
                    .map(Object::toString)
                    .orElse("");

            LOG.info("[infra.rest-consumer] (extract-token) token was extracted: {}", token);

            return Mono.just(token)
                    .switchIfEmpty(Mono.error(new TokenIsMissingException("Token is missing")));
        });
    }

}
