package org.pragma.creditya;

import org.junit.jupiter.api.*;
import org.mockserver.client.MockServerClient;
import org.pragma.creditya.api.dto.request.CreateApplicationLoanRequest;
import org.pragma.creditya.api.dto.response.ErrorResponse;
import org.pragma.creditya.api.dto.response.LoanAppliedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@Disabled
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class LoanIntegrationTest {

    private  static final String ENDPOINT_USER_EXIST = "/api/users/exists";

    @Container
    private static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withInitScript("db/init/schema.sql");


    @Container
    static MockServerContainer mockServer = new MockServerContainer(DockerImageName.parse("mockserver/mockserver:5.15.0"));


    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("adapters.r2dbc.host", postgres::getHost);
        registry.add("adapters.r2dbc.username", postgres::getUsername);
        registry.add("adapters.r2dbc.password", postgres::getPassword);
        registry.add("adapters.r2dbc.database", postgres::getDatabaseName);
        registry.add("adapters.r2dbc.port", () -> postgres.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT));

        registry.add("adapter.restconsumer.url", () ->  mockServer.getEndpoint() + ENDPOINT_USER_EXIST);
    }

    private static MockServerClient mockServerClient;

    @BeforeAll
    static void beforeAll() {
        postgres.start();
        mockServer.start();

        mockServerClient = new MockServerClient(mockServer.getHost(), mockServer.getServerPort());
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
        mockServer.stop();
    }

    @BeforeEach
    void setup () {
        mockServerClient.reset();
    }

    @Autowired
    private WebTestClient webTestClient;

    private final String URL_POST_APPLICATION_LOAN = "/api/loan";

    private final CreateApplicationLoanRequest REQUEST_EXAMPLE = new CreateApplicationLoanRequest(
            "103", BigDecimal.valueOf(4_000_000), 1L, 1, 0
    );

    private final CreateApplicationLoanRequest REQUEST_LOAN_TYPE_NOT_FOUND= new CreateApplicationLoanRequest(
            "103", BigDecimal.valueOf(4_000_000), 1001L, 1, 0
    );

    private final String TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyb2JpbkBjcmVkaXQuY29tIiwicm9sZXMiOlt7ImF1dGhvcml0eSI6IkNVU1RPTUVSIn1dLCJpYXQiOjE3NTczOTMzMDEsImV4cCI6MTc1NzM5NjkwMX0.NOCsmPZbJsKITMwYkQX07PYUgZKLFRyFYlwSY7P9D0k";

    @Test
    void shouldBeStatus200_becauseApplicationIsValid() {
        mockServerClient.when(request()
                        .withMethod("GET")
                        .withPath(ENDPOINT_USER_EXIST))
                .respond(response()
                        .withStatusCode(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"exists\":true}"));

        webTestClient.post()
                .uri(URL_POST_APPLICATION_LOAN)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN)
                .bodyValue(REQUEST_EXAMPLE)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(LoanAppliedResponse.class)
                .value(persisted -> {
                    assertNotNull(persisted.loanId());
                    assertFalse(persisted.loanId().isBlank());
                    assertEquals("PENDING", persisted.status());
                    assertEquals("103", persisted.document());
                    assertEquals(BigDecimal.valueOf(4_000_000), persisted.amount());
                    assertEquals(1L, persisted.loanTypeId());
                });
    }

    @Test
    void shouldBeStatus404_becauseCustomerIsNotFound () {
        mockServerClient.when(request()
                        .withMethod("GET")
                        .withPath(ENDPOINT_USER_EXIST))
                .respond(response()
                        .withStatusCode(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"exists\":false}"));

        webTestClient.post()
                .uri(URL_POST_APPLICATION_LOAN)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(REQUEST_EXAMPLE)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorResponse.class)
                .value(response -> {
                    assertEquals(404, response.status());
                    assertEquals("Document 103 does not exist, you need to check", response.message());
                });
    }

    @Test
    void shouldBeStatus404_becauseLoanTypeNotFound () {
        mockServerClient.when(request()
                        .withMethod("GET")
                        .withPath(ENDPOINT_USER_EXIST))
                .respond(response()
                        .withStatusCode(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"exists\":true}"));

        webTestClient.post()
                .uri(URL_POST_APPLICATION_LOAN)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(REQUEST_LOAN_TYPE_NOT_FOUND)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorResponse.class)
                .value(response -> {
                    assertEquals(404, response.status());
                    assertEquals("Type Loan code 1001 does not exist, you need to check", response.message());
                });
    }


}
