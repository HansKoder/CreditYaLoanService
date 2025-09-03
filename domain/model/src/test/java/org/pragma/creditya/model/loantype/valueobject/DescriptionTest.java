package org.pragma.creditya.model.loantype.valueobject;

import org.junit.jupiter.api.Test;
import org.pragma.creditya.model.loantype.exception.LoanTypeDomainException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DescriptionTest {

    @Test
    void shouldThrowException_whenDescriptionIsNull () {
        LoanTypeDomainException exception = assertThrows(LoanTypeDomainException.class, () -> {
            new Description(null);
        });

        assertEquals("Description Loan Type must be mandatory", exception.getMessage());
    }

    @Test
    void shouldThrowException_whenDescriptionIsEmpty () {
        LoanTypeDomainException exception = assertThrows(LoanTypeDomainException.class, () -> {
            new Description("");
        });

        assertEquals("Description Loan Type must be mandatory", exception.getMessage());
    }

    @Test
    void shouldBeCreatedWithSuccessful () {
        Description vo = new Description("Home");
        assertEquals("Home", vo.value());
    }

}
