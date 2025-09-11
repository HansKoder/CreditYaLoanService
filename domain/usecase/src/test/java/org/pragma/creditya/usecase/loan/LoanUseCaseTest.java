package org.pragma.creditya.usecase.loan;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pragma.creditya.model.loan.exception.LoanDomainException;
import org.pragma.creditya.model.loan.gateways.CustomerClient;
import org.pragma.creditya.model.loan.gateways.UserInfoRepository;
import org.pragma.creditya.usecase.command.CreateRequestLoanCommand;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class LoanUseCaseTest {

    @Mock
    private CustomerClient userClient;

    @Mock
    private UserInfoRepository userInfoRepository;

    @InjectMocks
    private LoanUseCase loanUseCase;

    @BeforeEach
    void setup () {
        userClient = Mockito.mock(CustomerClient.class);
        userInfoRepository = Mockito.mock(UserInfoRepository.class);

        loanUseCase = new LoanUseCase(userClient, userInfoRepository);
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
        CreateRequestLoanCommand cmd = new CreateRequestLoanCommand("123", BigDecimal.valueOf(150000), 1L, 1,6);

        StepVerifier.create(loanUseCase.checkApplication(cmd))
                .expectNextMatches(entity ->
                    !Objects.isNull(entity) && entity.getAmount().amount().equals(BigDecimal.valueOf(150000))
                )
                .verifyComplete();
    }

}
