package org.pragma.creditya.api.config;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class CorrelationIdConfig implements WebFilter {

    private static final String CORRELATION_ID_HEADER = "X-Correlation-Id";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String correlationId = exchange.getRequest()
                .getHeaders()
                .getFirst(CORRELATION_ID_HEADER);

        if (correlationId == null || correlationId.isBlank()) {
            correlationId = "ABC-101"; // UlidCreator.getUlid().toString(); // No bloqueante
        }

        String finalCorrelationId = correlationId;

        exchange.getResponse()
                .getHeaders()
                .add(CORRELATION_ID_HEADER, finalCorrelationId);

        return chain.filter(exchange)
                .contextWrite(ctx -> ctx.put(CORRELATION_ID_HEADER, finalCorrelationId))
                .doOnEach(signal -> {
                    /*
                    if (signal.isOnNext() || signal.isOnComplete() || signal.isOnError()) {
                        MDC.put(CORRELATION_ID_HEADER, finalCorrelationId);
                    }
                     */

                    MDC.put(CORRELATION_ID_HEADER, finalCorrelationId);
                })
                .doFinally(signal -> MDC.remove(CORRELATION_ID_HEADER));
    }


}
