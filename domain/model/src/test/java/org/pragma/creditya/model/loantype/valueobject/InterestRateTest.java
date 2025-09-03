package org.pragma.creditya.model.loantype.valueobject;

import org.junit.jupiter.api.Test;
import org.pragma.creditya.model.loantype.exception.LoanTypeDomainException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InterestRateTest {

    @Test
    void shouldThrowException_whenInterestRateIsNull() {
        LoanTypeDomainException exception = assertThrows(LoanTypeDomainException.class, () -> {
            new InterestRate(null);
        });

        assertEquals("Interest Rate must be mandatory", exception.getMessage());
    }

    @Test
    void shouldThrowException_whenDescriptionIsEmpty () {
        LoanTypeDomainException exception = assertThrows(LoanTypeDomainException.class, () -> {
            new InterestRate(-5D);
        });

        assertEquals("Interest Rate must be positive", exception.getMessage());
    }

    @Test
    void shouldBeCreatedWithSuccessful () {
        InterestRate vo = new InterestRate(0.5D);
        assertEquals(0.5D, vo.value());
    }

}
