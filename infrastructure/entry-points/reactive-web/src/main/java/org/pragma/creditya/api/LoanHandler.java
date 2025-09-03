package org.pragma.creditya.api;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.api.dto.request.CreateApplicationLoanRequest;
import org.pragma.creditya.api.mapper.LoanRestMapper;
import org.pragma.creditya.usecase.IOrchestratorUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class LoanHandler {

    private final IOrchestratorUseCase useCase;

    private final static Logger log = LoggerFactory.getLogger(LoanHandler.class);

    public Mono<ServerResponse> applicationLoan(ServerRequest serverRequest) {
        return Mono.deferContextual(ctx -> {
            String correlationId = ctx.get("X-Correlation-Id");
            log.info("CorrelationId[{}] [infra.reactive-web] (applicationLoan) 1 - create new application", correlationId);
            return serverRequest.bodyToMono(CreateApplicationLoanRequest.class)
                    .map(LoanRestMapper::toCommand)
                    .doOnSuccess(response -> log.info("CorrelationId[{}] [infra.reactive-web] (applicationLoan) 1.2 - map to command, payload= command:{}", correlationId, response))
                    .flatMap(useCase::applicationLoan)
                    .map(LoanRestMapper::toResponse)
                    .doOnSuccess(response -> log.info("CorrelationId[{}] [infra.reactive-web] (applicationLoan) 1.3 - application was persisted with successful, payload=response:{}", correlationId, response))
                    .flatMap(data -> ServerResponse.status(HttpStatus.CREATED).bodyValue(data));

        });

    }
}
