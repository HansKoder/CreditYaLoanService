package org.pragma.creditya.model.loan.valueobject;

import org.junit.jupiter.api.Test;
import org.pragma.creditya.model.loan.exception.LoanDomainException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    void shouldCreateAmountWithSuccessful () {
        Amount amount = new Amount(BigDecimal.valueOf(10));
        assertEquals(BigDecimal.TEN, amount.amount());
    }


}
