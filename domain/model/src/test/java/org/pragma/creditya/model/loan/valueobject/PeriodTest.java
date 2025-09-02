package org.pragma.creditya.model.loan.valueobject;


import org.junit.jupiter.api.Test;
import org.pragma.creditya.model.loan.exception.LoanDomainException;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PeriodTest {

    @Test
    void shouldThrowExceptionWhenYearIsNegative () {
        LoanDomainException exception = assertThrows(LoanDomainException.class, () ->
                new Period(-1,0));

        assertEquals("Year must be greater or equal to zero", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenMonthIsNegative () {
        LoanDomainException exception = assertThrows(LoanDomainException.class, () ->
                new Period(0,-1));

        assertEquals("Month must be greater or equal to zero", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenPeriodDoesNotHaveTime () {
        LoanDomainException exception = assertThrows(LoanDomainException.class, () ->
                new Period(0,0));

        assertEquals("Must be scheduled any period of payment", exception.getMessage());
    }

    @Test
    void shouldCreateNewPeriod () {
        Period period = new Period(1,2);

        assertEquals(1, period.year());
        assertEquals(2, period.month());
        assertEquals(14, period.calculateTotalMonths());
    }

}
