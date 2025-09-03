package org.pragma.creditya.api.config;

import org.pragma.creditya.api.LoanHandler;
import org.pragma.creditya.api.LoanRouterRest;
import org.junit.jupiter.api.Test;
import org.pragma.creditya.api.dto.request.CreateApplicationLoanRequest;
import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loan.valueobject.LoanStatus;
import org.pragma.creditya.usecase.IOrchestratorUseCase;
import org.pragma.creditya.usecase.loan.ILoanUseCase;
import org.pragma.creditya.usecase.command.CreateRequestLoanCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {LoanRouterRest.class, LoanHandler.class})
@WebFluxTest
@Import({CorsConfig.class, SecurityHeadersConfig.class})
class ConfigTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    IOrchestratorUseCase useCase;

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
    void corsConfigurationShouldAllowOrigins() {
        when(useCase.applicationLoan(any(CreateRequestLoanCommand.class)))
                .thenReturn(Mono.just(LOAN_EXAMPLE));

        webTestClient.post()
                .uri(URL_POST_APPLICATION_LOAN)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(REQUEST_EXAMPLE)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().valueEquals("Content-Security-Policy",
                        "default-src 'self'; frame-ancestors 'self'; form-action 'self'")
                .expectHeader().valueEquals("Strict-Transport-Security", "max-age=31536000;")
                .expectHeader().valueEquals("X-Content-Type-Options", "nosniff")
                .expectHeader().valueEquals("Server", "")
                .expectHeader().valueEquals("Cache-Control", "no-store")
                .expectHeader().valueEquals("Pragma", "no-cache")
                .expectHeader().valueEquals("Referrer-Policy", "strict-origin-when-cross-origin");
    }

}