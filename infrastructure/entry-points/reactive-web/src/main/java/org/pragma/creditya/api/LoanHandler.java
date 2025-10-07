package org.pragma.creditya.api;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.api.dto.request.CreateApplicationLoanRequest;
import org.pragma.creditya.api.dto.request.GetLoanRequest;
import org.pragma.creditya.api.dto.request.ResolutionApplicationLoanRequest;
import org.pragma.creditya.api.mapper.GetLoanMapper;
import org.pragma.creditya.api.mapper.LoanRestMapper;
import org.pragma.creditya.usecase.query.IQuery;
import org.pragma.creditya.usecase.query.handler.loan.dto.LoanSummaryDTO;
import org.pragma.creditya.usecase.service.ILoanApplicationService;
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

    private final ILoanApplicationService command;
    private final IQuery query;

    private final static Logger log = LoggerFactory.getLogger(LoanHandler.class);

    public Mono<ServerResponse> applicationLoan(ServerRequest serverRequest) {
            return serverRequest.bodyToMono(CreateApplicationLoanRequest.class)
                    .map(LoanRestMapper::toCommand)
                    .doOnSuccess(response -> log.info("[infra.reactive-web] (applicationLoan) 1.2 - map to command, payload= command:{}", response))
                    .flatMap(command::createApplicationSubmitLoan)
                    .map(LoanRestMapper::toResponse)
                    .doOnSuccess(response -> log.info("[infra.reactive-web] (applicationLoan) 1.3 - application was persisted with successful, payload=response:{}", response))
                    .flatMap(data -> ServerResponse.status(HttpStatus.CREATED).bodyValue(data));
    }

    public Mono<ServerResponse> getLoans(ServerRequest serverRequest) {
        int page = Integer.parseInt(serverRequest.queryParam("page").orElse("0"));
        int size = Integer.parseInt(serverRequest.queryParam("size").orElse("10"));
        String document = serverRequest.queryParam("document").orElse("");
        String status = serverRequest.queryParam("status").orElse("");

        GetLoanRequest request = new GetLoanRequest(status, document, page, size);

        log.info("[infra.reactive-web] (getLoansQuery) filters: {}", request);

        return query.getLoans(GetLoanMapper.toQuery(request))
                .doOnNext(response -> log.info("[infra.reactive-web] (getLoansQuery) result: {}", response))
                .as(data -> ServerResponse.ok().body(data, LoanSummaryDTO.class));
    }

    public Mono<ServerResponse> resolutionApplicationLoan (ServerRequest serverRequest) {
        return serverRequest.bodyToMono(ResolutionApplicationLoanRequest.class)
                .map(LoanRestMapper::toCommand)
                .doOnSuccess(response -> log.info("[infra.reactive-web] (decisionLoan) 1.2 - map to command, payload= command:{}", response))
                .flatMap(command::resolutionApplicationLoan)
                .map(LoanRestMapper::toResponse)
                .doOnSuccess(response -> log.info("[infra.reactive-web] (decisionLoan) 1.3 - loan was resolved with successful, payload=response:{}", response))
                .flatMap(data -> ServerResponse.status(HttpStatus.CREATED).bodyValue(data));
    }

}
