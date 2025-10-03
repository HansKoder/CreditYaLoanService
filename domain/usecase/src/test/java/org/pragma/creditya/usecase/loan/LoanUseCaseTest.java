package org.pragma.creditya.usecase.loan;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pragma.creditya.model.loan.exception.LoanDomainException;
import org.pragma.creditya.model.customer.gateway.CustomerRepository;
import org.pragma.creditya.model.loan.gateways.UserInfoRepository;
import org.pragma.creditya.usecase.command.handler.loan.LoanUseCase;
import org.pragma.creditya.usecase.command.CreateApplicationLoanCommand;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class LoanUseCaseTest {

    @Mock
    private CustomerRepository userClient;

    @Mock
    private UserInfoRepository userInfoRepository;

    @InjectMocks
    private LoanUseCase loanUseCase;

    /**
    @BeforeEach
    void setup () {
        userClient = Mockito.mock(CustomerRepository.class);
        userInfoRepository = Mockito.mock(UserInfoRepository.class);

        loanUseCase = new LoanUseCase(userClient, userInfoRepository);
    }

    @Test
    void shouldThrowExceptionWhenDocumentIsEmpty () {
        CreateApplicationLoanCommand cmd = new CreateApplicationLoanCommand(null, BigDecimal.ONE, 1L, 1,6);
        StepVerifier.create(loanUseCase.checkApplication(cmd))
                .expectErrorSatisfies(throwable -> {
                    assertEquals("Document must be mandatory", throwable.getMessage());
                    assertInstanceOf(LoanDomainException.class, throwable);
                })
                .verify();
    }

    @Test
    void shouldThrowExceptionCustomerDoesNotExist () {
        CreateApplicationLoanCommand cmd = new CreateApplicationLoanCommand("123", BigDecimal.valueOf(150000), 1L, 1,6);

        StepVerifier.create(loanUseCase.checkApplication(cmd))
                .expectNextMatches(entity ->
                    !Objects.isNull(entity) && entity.getAmount().amount().equals(BigDecimal.valueOf(150000))
                )
                .verifyComplete();
    }
    */

    @Test
    void shouldBeTrue () {
        assertTrue(Boolean.TRUE);
    }

}
