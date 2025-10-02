package org.pragma.creditya.outbox.strategy.selfdecision;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loan.entity.CustomerRead;
import org.pragma.creditya.model.loan.valueobject.LoanStatus;
import org.pragma.creditya.model.loanread.LoanRead;
import org.pragma.creditya.usecase.outbox.payload.DebtPayload;
import org.pragma.creditya.usecase.outbox.payload.DecisionLoanOutboxPayload;
import org.pragma.creditya.usecase.outbox.strategy.selfdecision.SelfDecisionMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ResolutionTypeMapperTest {

    private final String AGGREGATE_ID = "c4fc5172-1cd3-4dfa-aa71-456b50bc9089";

    @DisplayName("Should Be mapped without debts because it does not have previous debts.")
    @Test
    void shouldBeMapped_withoutDebts_becauseItDoestNotHavePreviousDebts () {
        Loan domain = Loan.LoanBuilder.aLoan()
                .id(UUID.fromString(AGGREGATE_ID))
                .loanStatus(LoanStatus.PENDING)
                .amount(BigDecimal.valueOf(1200000))
                .build();

        CustomerRead customerRead = CustomerRead.builder()
                .baseSalary(BigDecimal.valueOf(5000000))
                .build();

        DecisionLoanOutboxPayload payload = SelfDecisionMapper.toPayload(domain, List.of(), customerRead);

        assertNotNull(payload);
        assertEquals(0, payload.getDebts().size());
        assertEquals(BigDecimal.valueOf(5000000), payload.getCustomerSalary());
        assertEquals(AGGREGATE_ID, payload.getCurrentLoanId());
        assertEquals(BigDecimal.valueOf(1200000), payload.getCurrentLoanAmount());
    }

    @DisplayName("Should Be mapped without debts because it does not have previous debts.")
    @Test
    void shouldBeMapped_withPreviousDebts_becauseItHasPreviousDebts () {
        LoanRead loanRead = LoanRead.builder()
                .loanId(UUID.fromString(AGGREGATE_ID))
                .amount(BigDecimal.valueOf(1200000))
                .totalMonthlyDebt(BigDecimal.valueOf(100000))
                .build();

        Loan domain = Loan.LoanBuilder.aLoan()
                .id(UUID.fromString(AGGREGATE_ID))
                .loanStatus(LoanStatus.PENDING)
                .amount(BigDecimal.valueOf(1200000))
                .build();

        CustomerRead customerRead = CustomerRead.builder()
                .baseSalary(BigDecimal.valueOf(5000000))
                .build();

        DecisionLoanOutboxPayload payload = SelfDecisionMapper.toPayload(domain, List.of(loanRead), customerRead);

        assertNotNull(payload);

        assertEquals(1, payload.getDebts().size());

        DebtPayload firstPreviousDebt = payload.getDebts().getFirst();
        assertEquals(AGGREGATE_ID, firstPreviousDebt.loanId());
        assertEquals(BigDecimal.valueOf(100000), firstPreviousDebt.price());

        assertEquals(BigDecimal.valueOf(5000000), payload.getCustomerSalary());
        assertEquals(AGGREGATE_ID, payload.getCurrentLoanId());
        assertEquals(BigDecimal.valueOf(1200000), payload.getCurrentLoanAmount());
    }

}
