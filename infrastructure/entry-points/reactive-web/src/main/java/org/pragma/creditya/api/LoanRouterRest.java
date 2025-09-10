package org.pragma.creditya.api;

import org.pragma.creditya.api.dto.response.ErrorResponse;
import org.pragma.creditya.infracommon.exception.InfrastructureException;
import org.pragma.creditya.model.loan.exception.DocumentNotFoundDomainException;
import org.pragma.creditya.model.loan.exception.LoanDomainException;
import org.pragma.creditya.model.loantype.exception.LoanTypeNotFoundDomainException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class LoanRouterRest {
    @Bean
    public RouterFunction<ServerResponse> routerFunction(LoanHandler handler) {
        return route(POST("/api/v1/loan"), handler::applicationLoan)
                .filter(domainHandlerExceptions())
                .filter(infraHandlerExceptions())
                .andRoute(GET("/api/v1/loans"), handler::getLoans)
                .filter(domainHandlerExceptions())
                .filter(infraHandlerExceptions());
    }

    private HandlerFilterFunction<ServerResponse, ServerResponse> domainHandlerExceptions() {
        return (request, next) ->
                next.handle(request)
                        .onErrorResume(LoanDomainException.class, ex ->
                                ServerResponse.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage()))
                        ).onErrorResume(LoanTypeNotFoundDomainException.class, ex ->
                                ServerResponse.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage()))
                        ).onErrorResume(DocumentNotFoundDomainException.class, ex ->
                                ServerResponse.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage()))
                        );
    }

    private HandlerFilterFunction<ServerResponse, ServerResponse> infraHandlerExceptions() {
        return (request, next) ->
                next.handle(request)
                        .onErrorResume(InfrastructureException.class, ex ->
                                ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()))
                        )
                        .onErrorResume(Exception.class, ex ->
                                ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()))
                        );
    }
}
