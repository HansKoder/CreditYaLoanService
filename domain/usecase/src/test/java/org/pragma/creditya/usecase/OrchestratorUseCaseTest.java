package org.pragma.creditya.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loan.gateways.EventStoreRepository;
import org.pragma.creditya.model.loan.valueobject.LoanStatus;
import org.pragma.creditya.usecase.command.CreateRequestLoanCommand;
import org.pragma.creditya.usecase.loan.ILoanUseCase;
import org.pragma.creditya.usecase.loan.LoanUseCase;
import org.pragma.creditya.usecase.loantype.ILoanTypeUseCase;
import org.pragma.creditya.usecase.loantype.LoanTypeUseCase;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
public class OrchestratorUseCaseTest {

    @Mock
    private ILoanUseCase loanUseCase;

    @Mock
    private ILoanTypeUseCase loanTypeUseCase;

    @Mock
    private EventStoreRepository eventStoreRepository;

    @InjectMocks
    private OrchestratorUseCase orchestratorUseCase;

    private final Loan LOAN_EXAMPLE = Loan.LoanBuilder.aLoan()
            .loanStatus(LoanStatus.PENDING)
            .amount(BigDecimal.valueOf(4_000_000))
            .loanType(1L)
            .period(1,0)
            .document("103")
            .build();

    @BeforeEach
    void setup() {
        loanUseCase = Mockito.mock(LoanUseCase.class);
        loanTypeUseCase = Mockito.mock(LoanTypeUseCase.class);
        eventStoreRepository = Mockito.mock(EventStoreRepository.class);

        orchestratorUseCase = new OrchestratorUseCase(loanTypeUseCase, loanUseCase, eventStoreRepository);
    }


    @Test
    void shouldBePersisted_becauseApplicationLoanIsValid () {
        CreateRequestLoanCommand createRequestLoanCommand = new CreateRequestLoanCommand("103", BigDecimal.valueOf(4_000_000), 1L, 1,6);

        Mockito.when(loanUseCase.checkApplication(createRequestLoanCommand))
                .thenReturn(Mono.just(LOAN_EXAMPLE));

        Mockito.when(loanTypeUseCase.checkLoanTypeExists(LOAN_EXAMPLE))
                .thenReturn(Mono.just(LOAN_EXAMPLE));

        Mockito.when(loanUseCase.persist(LOAN_EXAMPLE))
                .thenReturn(Mono.just(LOAN_EXAMPLE));


        var response = orchestratorUseCase.applicationLoan(createRequestLoanCommand);

        StepVerifier.create(response)
                .expectNext(LOAN_EXAMPLE)
                .verifyComplete();
    }

}
