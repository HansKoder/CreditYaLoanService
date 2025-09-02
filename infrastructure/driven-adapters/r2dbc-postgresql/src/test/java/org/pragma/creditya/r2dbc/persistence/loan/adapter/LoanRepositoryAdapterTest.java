package org.pragma.creditya.r2dbc.persistence.loan.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loan.valueobject.LoanStatus;
import org.pragma.creditya.r2dbc.persistence.loan.entity.LoanEntity;
import org.pragma.creditya.r2dbc.persistence.loan.entity.LoanStatusEntity;
import org.pragma.creditya.r2dbc.persistence.loan.mapper.LoanMapper;
import org.pragma.creditya.r2dbc.persistence.loan.repository.LoanReactiveRepository;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanRepositoryAdapterTest {
    // TODO: change four you own tests

    @InjectMocks
    LoanRepositoryAdapter repositoryAdapter;

    @Mock
    LoanReactiveRepository repository;

    @Mock
    LoanMapper mapper;

    @BeforeEach
    void setup () {
        repository = Mockito.mock(LoanReactiveRepository.class);
        mapper = Mockito.mock(LoanMapper.class);

        repositoryAdapter = new LoanRepositoryAdapter(repository, mapper);
    }

    private final UUID LOAN_ID_EXAMPLE = UUID.fromString("5b87a0d6-2fed-4db7-aa49-49663f719659");

    private final Loan LOAN_ENTITY_EXAMPLE = Loan.LoanBuilder.aLoan()
            .id(LOAN_ID_EXAMPLE)
            .loanType(1L)
            .loanStatus(LoanStatus.PENDING)
            .document("103")
            .period(1,0)
            .amount(BigDecimal.ONE)
            .build();

    private final LoanEntity LOAN_DATA_EXAMPLE = LoanEntity
            .builder()
            .loanId(LOAN_ID_EXAMPLE)
            .status(LoanStatusEntity.PENDING)
            .month(0)
            .year(1)
            .document("103")
            .build();

    @Test
    void mustSaveValue() {

        when(repository.save(any())).thenReturn(Mono.just(LOAN_DATA_EXAMPLE));
        when(mapper.toData(any())).thenReturn(LOAN_DATA_EXAMPLE);
        when(mapper.toEntity(any())).thenReturn(LOAN_ENTITY_EXAMPLE);

        StepVerifier.create(repositoryAdapter.save(LOAN_ENTITY_EXAMPLE))
                .expectNextMatches(value -> value.equals(LOAN_ENTITY_EXAMPLE))
                .verifyComplete();
    }

}
