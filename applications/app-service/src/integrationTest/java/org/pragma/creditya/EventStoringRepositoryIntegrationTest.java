package org.pragma.creditya;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.pragma.creditya.model.loan.event.*;
import org.pragma.creditya.model.loan.event.payload.ApplicationSubmittedEvent;
import org.pragma.creditya.model.loan.gateways.EventStoreRepository;
import org.pragma.creditya.model.loan.valueobject.LoanStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class EventStoringRepositoryIntegrationTest {

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
    private EventStoreRepository eventStoreRepository;

    @Test
    void shouldBePersistedWithSuccessful_eventApplicationLoan() {

        var payload = ApplicationSubmittedEvent.builder()
                .document("123")
                .typeLoan(1L)
                .amount(BigDecimal.valueOf(1))
                .status(LoanStatus.PENDING)
                .build();

        var event = LoanEvent.builder()
                .aggregateType(AggregateType.AGGREGATE_LOAN)
                .eventType(EventType.LOAN_SUBMITTED)
                .payload(payload)
                .build();

        StepVerifier.create(eventStoreRepository.saveAll(List.of(event)))
                .verifyComplete();
    }


}
