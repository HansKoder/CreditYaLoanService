package org.pragma.creditya.model.loantype;

import org.junit.jupiter.api.Test;
import org.pragma.creditya.model.loantype.exception.LoanTypeDomainException;

import static org.junit.jupiter.api.Assertions.*;

public class LoanTypeTest {

    @Test
    void shouldThrowException_whenDescriptionIsNull () {
        LoanTypeDomainException exception = assertThrows(LoanTypeDomainException.class, () -> {
            LoanType.LoanTypeBuilder
                    .aLoanType()
                    .build();
        });

        assertEquals("Description Loan Type must be mandatory", exception.getMessage());
    }

    @Test
    void shouldThrowException_whenInterestRateIsNull () {
        LoanTypeDomainException exception = assertThrows(LoanTypeDomainException.class, () -> {
            LoanType.LoanTypeBuilder
                    .aLoanType()
                    .description("Home")
                    .build();
        });

        assertEquals("Interest Rate must be mandatory", exception.getMessage());
    }

    @Test
    void shouldBeCratedDomainWithSuccessful () {
        LoanType loanType = LoanType.LoanTypeBuilder
                .aLoanType()
                .description("Home")
                .interestRate(0.4D)
                .build();

        assertNotNull(loanType);
        assertNull(loanType.getId().getValue());

        assertEquals("Home", loanType.getDescription().value());
        assertEquals(0.4D, loanType.getInterestRate().value());
    }

}
