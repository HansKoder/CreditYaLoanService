package org.pragma.creditya.api;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.api.dto.request.CreateApplicationLoanRequest;
import org.pragma.creditya.api.mapper.LoanRestMapper;
import org.pragma.creditya.usecase.IOrchestratorUseCase;
import org.pragma.creditya.usecase.loan.ILoanUseCase;
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

    public Mono<ServerResponse> applicationLoan(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CreateApplicationLoanRequest.class)
                .map(LoanRestMapper::toCommand)
                .flatMap(useCase::applicationLoan)
                .map(LoanRestMapper::toResponse)
                .flatMap(data -> ServerResponse.status(HttpStatus.CREATED).bodyValue(data));

    }
}
