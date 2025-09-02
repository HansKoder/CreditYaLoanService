package org.pragma.creditya.usecase.loan;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loan.exception.DocumentNotFoundDomainException;
import org.pragma.creditya.model.loan.exception.LoanDomainException;
import org.pragma.creditya.model.loan.valueobject.LoanStatus;
import org.pragma.creditya.model.loantype.exception.LoanTypeNotFoundDomainException;
import org.pragma.creditya.model.loan.gateways.LoanRepository;
import org.pragma.creditya.model.loan.gateways.LoanTypeRepository;
import org.pragma.creditya.model.loan.gateways.CustomerClient;
import org.pragma.creditya.usecase.command.CreateRequestLoanCommand;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class LoanUseCaseTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private CustomerClient userClient;

    @InjectMocks
    private LoanUseCase loanUseCase;

    @BeforeEach
    void setup () {
        userClient = Mockito.mock(CustomerClient.class);
        loanRepository = Mockito.mock(LoanRepository.class);

        loanUseCase = new LoanUseCase(loanRepository, userClient);
    }

    @Test
    void shouldThrowExceptionWhenDocumentIsEmpty () {
        CreateRequestLoanCommand cmd = new CreateRequestLoanCommand(null, BigDecimal.ONE, 1L, 1,6);
        StepVerifier.create(loanUseCase.checkApplication(cmd))
                .expectErrorSatisfies(throwable -> {
                    assertEquals("Document must be mandatory", throwable.getMessage());
                    assertInstanceOf(LoanDomainException.class, throwable);
                })
                .verify();
    }

    @Test
    void shouldThrowExceptionCustomerDoesNotExist () {
        CreateRequestLoanCommand cmd = new CreateRequestLoanCommand("123", BigDecimal.ONE, 1L, 1,6);

        Mockito.when(userClient.exitByDocument("123"))
                .thenReturn(Mono.just(Boolean.FALSE));

        StepVerifier.create(loanUseCase.checkApplication(cmd))
                .expectErrorSatisfies(throwable -> {
                    assertEquals("Document 123 does not exist, you need to check", throwable.getMessage());
                    assertInstanceOf(DocumentNotFoundDomainException.class, throwable);
                })
                .verify();
    }


    @Test
    void shouldThrowExceptionRepositoryIsNotWorking () {
        Loan loan = Loan.LoanBuilder.aLoan()
                .loanStatus(LoanStatus.PENDING)
                .amount(BigDecimal.valueOf(4_000_000))
                .loanType(1L)
                .period(1,0)
                .document("103")
                .build();

        Mockito.when(loanRepository.save(ArgumentMatchers.any()))
                        .thenReturn(Mono.error(new SQLException("constraint unique invalid")));

        StepVerifier.create(loanUseCase.persist(loan))
                .expectErrorSatisfies(throwable -> {
                    assertEquals("constraint unique invalid", throwable.getMessage());
                    assertInstanceOf(SQLException.class, throwable);
                })
                .verify();
    }

}
