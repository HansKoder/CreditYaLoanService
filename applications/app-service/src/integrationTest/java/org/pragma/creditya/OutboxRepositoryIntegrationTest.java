package org.pragma.creditya;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.pragma.creditya.model.loan.gateways.OutboxRepository;
import org.pragma.creditya.model.outbox.LoanOutboxMessage;
import org.pragma.creditya.model.outbox.OutboxStatus;
import org.pragma.creditya.usecase.outbox.payload.NotificationOutboxPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;

import java.util.UUID;

@SpringBootTest
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class OutboxRepositoryIntegrationTest {

    @Container
    private static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withInitScript("db/init/schema.sql");


    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("adapters.r2dbc.host", postgres::getHost);
        registry.add("adapters.r2dbc.username", postgres::getUsername);
        registry.add("adapters.r2dbc.password", postgres::getPassword);
        registry.add("adapters.r2dbc.database", postgres::getDatabaseName);
        registry.add("adapters.r2dbc.port", () -> postgres.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT));
    }

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @Autowired
    private OutboxRepository outboxRepository;

    private String LOAN_ID = "8045a685-104d-4093-99b4-ba83bcb0ee2c";

    @Test
    void shouldBePersistedWithSuccessful_eventCustomerNotification() {

        NotificationOutboxPayload payload = NotificationOutboxPayload.builder()
                .subject("Loan Notification")
                .destination("doe@gmail.com")
                .message("Hello Mr Doe")
                .type("EMAIL")
                .build();

        LoanOutboxMessage outboxMessage = LoanOutboxMessage.builder()
                .type(payload.getType())
                .aggregateId(UUID.fromString(LOAN_ID))
                .status(OutboxStatus.COMPLETED)
                .aggregateName("LOAN")
                .build();

        StepVerifier.create(outboxRepository.saveOutboxMessage(outboxMessage, payload))
                .verifyComplete();
    }


}
