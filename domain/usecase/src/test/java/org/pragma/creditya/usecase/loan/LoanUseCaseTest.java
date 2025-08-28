package org.pragma.creditya.usecase.loan;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loan.exception.DocumentDoestExistDomainException;
import org.pragma.creditya.model.loan.exception.LoanDomainException;
import org.pragma.creditya.model.loan.gateways.LoanRepository;
import org.pragma.creditya.model.loan.gateways.UserClient;
import org.pragma.creditya.model.loan.valueobject.LoanId;
import org.pragma.creditya.model.loan.valueobject.LoanStatus;
import org.pragma.creditya.usecase.loan.command.CreateRequestLoanCommand;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
public class LoanUseCaseTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private UserClient userClient;

    @InjectMocks
    private LoanUseCase loanUseCase;

    @BeforeEach
    void setup () {
        userClient = Mockito.mock(UserClient.class);
        loanRepository = Mockito.mock(LoanRepository.class);

        loanUseCase = new LoanUseCase(loanRepository, userClient);
    }

    @Test
    void shouldThrowExceptionWhenDocumentIsEmpty () {
        CreateRequestLoanCommand cmd = new CreateRequestLoanCommand(null, BigDecimal.ONE, LocalDate.now().plusMonths(1));
        StepVerifier.create(loanUseCase.createRequestLoan(cmd))
                .expectErrorSatisfies(throwable -> {
                    assertEquals("Document must be mandatory", throwable.getMessage());
                    assertInstanceOf(LoanDomainException.class, throwable);
                })
                .verify();
    }

    /*
    @Test
    void shouldThrowExceptionWhenDocumentDoesNotExistUserService () {
        Mockito.when(userClient.exitByDocument(anyString()))
                .thenReturn(Mono.just(Boolean.FALSE));

        CreateRequestLoanCommand cmd = new CreateRequestLoanCommand("xxx", BigDecimal.ONE, LocalDate.now().plusMonths(1));
        StepVerifier.create(loanUseCase.createRequestLoan(cmd))
                .expectErrorSatisfies(throwable -> {
                    assertEquals("Document does not exist in the user service", throwable.getMessage());
                    assertInstanceOf(DocumentDoestExistDomainException.class, throwable);
                })
                .verify();
    }
    */

    @Test
    void shouldThrowExceptionWhenRepositoryIsNotWorking () {
                Mockito.when(loanRepository.save(any()))
                .thenThrow(new RuntimeException("DB is not working"));

        CreateRequestLoanCommand cmd = new CreateRequestLoanCommand("xxx", BigDecimal.ONE, LocalDate.now().plusMonths(1));
        StepVerifier.create(loanUseCase.createRequestLoan(cmd))
                .expectErrorSatisfies(throwable -> {
                    assertEquals("DB is not working", throwable.getMessage());
                    assertInstanceOf(Exception.class, throwable);
                })
                .verify();
    }

    @Test
    void shouldCreateRequestLoanWithSuccessful() {
        Loan expected = Loan.rebuild(UUID.fromString(
                "7ce77768-f25f-446a-9358-e7f0e1d298fe"),
                "xxx",
                BigDecimal.valueOf(10),
                LocalDate.now().plusMonths(2),
                LoanStatus.PENDING
        );

        Mockito.when(loanRepository.save(any()))
                .thenReturn(Mono.just(expected));

        CreateRequestLoanCommand cmd = new CreateRequestLoanCommand("xxx", BigDecimal.ONE, LocalDate.now().plusMonths(1));
        StepVerifier.create(loanUseCase.createRequestLoan(cmd))
                .expectNext(expected)
                .verifyComplete();
    }

}
