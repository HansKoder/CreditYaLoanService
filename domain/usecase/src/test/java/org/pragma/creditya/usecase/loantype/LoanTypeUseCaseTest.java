package org.pragma.creditya.usecase.loantype;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loan.valueobject.LoanStatus;
import org.pragma.creditya.model.loantype.exception.LoanTypeNotFoundDomainException;
import org.pragma.creditya.model.loantype.gateways.LoanTypeRepository;
import org.pragma.creditya.model.loantype.valueobject.LoanTypeId;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@ExtendWith(MockitoExtension.class)
public class LoanTypeUseCaseTest {

    @InjectMocks
    private LoanTypeUseCase loanTypeUseCase;

    @Mock
    private LoanTypeRepository loanTypeRepository;

    @BeforeEach
    void setup () {
        loanTypeRepository = Mockito.mock(LoanTypeRepository.class);
        loanTypeUseCase = new LoanTypeUseCase(loanTypeRepository);
    }

    private final Loan LOAN_EXAMPLE = Loan.LoanBuilder.aLoan()
            .loanStatus(LoanStatus.PENDING)
            .amount(BigDecimal.valueOf(4_000_000))
            .loanType(1L)
            .period(1,0)
            .document("103")
            .build();

    @Test
    void shouldThrowException_whenLoanTypeIsNotFound () {
        Mockito.when(loanTypeRepository.existLoanType(new LoanTypeId(1L)))
                .thenReturn(Mono.just(Boolean.FALSE));

        StepVerifier.create(loanTypeUseCase.checkLoanTypeExists(LOAN_EXAMPLE))
                .expectErrorSatisfies(throwable -> {
                    assertEquals("Type Loan code 1 does not exist, you need to check", throwable.getMessage());
                    assertInstanceOf(LoanTypeNotFoundDomainException.class, throwable);
                })
                .verify();
    }

    @Test
    void shouldReturnLoan_becauseLoanTypeExist () {
        Mockito.when(loanTypeRepository.existLoanType(new LoanTypeId(1L)))
                .thenReturn(Mono.just(Boolean.TRUE));

        var response = loanTypeUseCase.checkLoanTypeExists(LOAN_EXAMPLE);

        StepVerifier.create(response)
                .expectNext(LOAN_EXAMPLE)
                .verifyComplete();

    }

}
