package org.pragma.creditya.api;

import org.junit.jupiter.api.Test;
import org.pragma.creditya.api.dto.request.CreateApplicationLoanRequest;
import org.pragma.creditya.api.dto.response.ErrorResponse;
import org.pragma.creditya.api.dto.response.LoanAppliedResponse;
import org.pragma.creditya.infracommon.exception.InfrastructureException;
import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loan.exception.DocumentNotFoundDomainException;
import org.pragma.creditya.model.loan.exception.LoanDomainException;
import org.pragma.creditya.model.loan.exception.LoanTypeNotFoundDomainException;
import org.pragma.creditya.model.loan.valueobject.LoanStatus;
import org.pragma.creditya.usecase.loan.ILoanUseCase;
import org.pragma.creditya.usecase.loan.command.CreateRequestLoanCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


import static org.junit.jupiter.api.Assertions.assertEquals;


@ContextConfiguration(classes = {LoanRouterRest.class, LoanHandler.class})
@WebFluxTest
class RouterRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    ILoanUseCase useCase;

    private final String URL_POST_APPLICATION_LOAN = "/api/loan";

    private final UUID LOAN_ID_EXAMPLE = UUID.fromString("5b87a0d6-2fed-4db7-aa49-49663f719659");

    private final Loan LOAN_EXAMPLE = Loan.LoanBuilder.aLoan()
            .id(LOAN_ID_EXAMPLE)
            .loanType(1L)
            .loanStatus(LoanStatus.PENDING)
            .document("103")
            .period(1,0)
            .amount(BigDecimal.ONE)
            .build();

    private final CreateApplicationLoanRequest REQUEST_EXAMPLE = new CreateApplicationLoanRequest(
            "103",
            BigDecimal.ONE,
            1L,
            1,
            0
    );

    @Test
    void shouldCreateUserWithSuccessful() {
        when(useCase.createRequestLoan(any(CreateRequestLoanCommand.class)))
                .thenReturn(Mono.just(LOAN_EXAMPLE));

        webTestClient.post()
                .uri(URL_POST_APPLICATION_LOAN)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(REQUEST_EXAMPLE)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(LoanAppliedResponse.class)
                .value(persisted -> {
                    assertEquals("5b87a0d6-2fed-4db7-aa49-49663f719659", persisted.loanId());
                });
    }


    @Test
    void shouldThrowException_whenAmountIsNegative() {
        when(useCase.createRequestLoan(any(CreateRequestLoanCommand.class)))
                .thenReturn(Mono.error(new LoanDomainException("Amount must be positive")));

        webTestClient.post()
                .uri(URL_POST_APPLICATION_LOAN)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(REQUEST_EXAMPLE)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .value(response -> {
                    assertEquals(400, response.status());
                    assertEquals("Amount must be positive", response.message());
                });
    }

    @Test
    void shouldThrowException_whenDocumentIsNotFound() {
        when(useCase.createRequestLoan(any(CreateRequestLoanCommand.class)))
                .thenReturn(Mono.error(new DocumentNotFoundDomainException("Document not found")));

        webTestClient.post()
                .uri(URL_POST_APPLICATION_LOAN)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(REQUEST_EXAMPLE)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorResponse.class)
                .value(response -> {
                    assertEquals(404, response.status());
                    assertEquals("Document not found", response.message());
                });
    }

    @Test
    void shouldThrowException_whenLoanTypeNotFound() {
        when(useCase.createRequestLoan(any(CreateRequestLoanCommand.class)))
                .thenReturn(Mono.error(new LoanTypeNotFoundDomainException("LoanType not found")));

        webTestClient.post()
                .uri(URL_POST_APPLICATION_LOAN)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(REQUEST_EXAMPLE)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorResponse.class)
                .value(response -> {
                    assertEquals(404, response.status());
                    assertEquals("LoanType not found", response.message());
                });
    }

    @Test
    void shouldThrowException_whenCustomerClientIsDown() {
        when(useCase.createRequestLoan(any(CreateRequestLoanCommand.class)))
                .thenReturn(Mono.error(new InfrastructureException("Server is not working")));

        webTestClient.post()
                .uri(URL_POST_APPLICATION_LOAN)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(REQUEST_EXAMPLE)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody(ErrorResponse.class)
                .value(response -> {
                    assertEquals(500, response.status());
                    assertEquals("Server is not working", response.message());
                });
    }
}
