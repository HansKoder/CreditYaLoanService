package org.pragma.creditya.model.loan.valueobject;

import org.junit.jupiter.api.Test;
import org.pragma.creditya.model.loan.exception.LoanDomainException;
import org.pragma.creditya.model.shared.domain.model.valueobject.Amount;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class AmountTest {

    @Test
    void shouldThrowExceptionWhenAmountIsNull () {
        LoanDomainException exception = assertThrows(LoanDomainException.class, () -> {
            new Amount(null);
        });

        assertEquals("Amount must be mandatory", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenAmountIsNegative () {
        LoanDomainException exception = assertThrows(LoanDomainException.class, () -> {
            new Amount(BigDecimal.valueOf(-3));
        });

        assertEquals("Amount must be positive", exception.getMessage());
    }

    @Test
    void shouldBeFalseAmountIsLowerThanAnotherAmount () {
        BigDecimal greater = new BigDecimal(10);
        assertFalse(new Amount(BigDecimal.valueOf(3)).isGreaterThan(greater));
    }

    @Test
    void shouldBeTrueAmountIsGreaterThanAnotherAmount () {
        BigDecimal greater = new BigDecimal(10);
        assertTrue(new Amount(BigDecimal.valueOf(30)).isGreaterThan(greater));
    }


    @Test
    void shouldCreateAmountWithSuccessful () {
        Amount amount = new Amount(BigDecimal.valueOf(10));
        assertEquals(BigDecimal.TEN, amount.amount());
    }


}
