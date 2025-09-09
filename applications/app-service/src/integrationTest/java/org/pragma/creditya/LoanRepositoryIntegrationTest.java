package org.pragma.creditya;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loan.event.LoanApplicationSubmitted;
import org.pragma.creditya.model.loan.gateways.EventStoreRepository;
import org.pragma.creditya.model.loan.gateways.LoanRepository;
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
import java.util.Objects;
import java.util.UUID;

@SpringBootTest
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class LoanRepositoryIntegrationTest {

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
    private LoanRepository repository;

    @Autowired
    private EventStoreRepository eventStoreRepository;

    private final Loan LOAN_EXAMPLE = Loan.LoanBuilder.aLoan()
            .loanType(1L)
            .loanStatus(LoanStatus.PENDING)
            .document("103")
            .period(1,0)
            .amount(BigDecimal.valueOf(1_000_000))
            .build();


    @Test
    void shouldBePersistedWithSuccessful() {

        StepVerifier.create(repository.save(LOAN_EXAMPLE))
                .expectNextMatches(persisted -> !Objects.isNull(persisted)
                        && persisted.getId().getValue() != null
                        && !persisted.getId().getValue().toString().isBlank()
                        && persisted.getDocument().value().equals("103")
                        && persisted.getAmount().amount().equals(BigDecimal.valueOf(1_000_000))
                        && persisted.getLoanStatus().equals(LoanStatus.PENDING)
                        && persisted.getPeriod().year() == 1
                        && persisted.getPeriod().month() == 0
                )
                .verifyComplete();
    }

    @Test
    void shouldBePersistedWithSuccessful_eventApplicationLoan() {

        LoanApplicationSubmitted event = LoanApplicationSubmitted.LoanBuilder
                .aLoanApplicationSubmitted()
                .aggregateType("LOAN")
                .eventType(LoanApplicationSubmitted.class.getSimpleName())
                .document("123")
                .typeLoan(1L)
                .amount(BigDecimal.valueOf(1))
                .status("PENDING")
                .build();

        StepVerifier.create(eventStoreRepository.saveAll(List.of(event)))
                .verifyComplete();
    }


}
