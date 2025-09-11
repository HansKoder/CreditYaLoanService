package org.pragma.creditya.model.loan.valueobject;

import org.junit.jupiter.api.Test;
import org.pragma.creditya.model.loan.exception.LoanDomainException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LoanTypeTest {

    @Test
    void shouldThrowExceptionLoanTypeIsNull () {
        LoanDomainException exception = assertThrows(LoanDomainException.class, () -> {
            new LoanTypeCode(null);
        });

        assertEquals("Loan Type must be mandatory", exception.getMessage());
    }

    @Test
    void shouldCreateLoanTypeWithSuccessful () {
        LoanTypeCode vo = new LoanTypeCode(1L);

        assertEquals(1L, vo.code());
    }

}
