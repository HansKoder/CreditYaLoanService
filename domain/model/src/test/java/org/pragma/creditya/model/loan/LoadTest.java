package org.pragma.creditya.model.loan;

import org.junit.jupiter.api.Test;
import org.pragma.creditya.model.loan.exception.LoanDomainException;
import org.pragma.creditya.model.loan.valueobject.LoanStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class LoadTest {

    @Test
    void shouldThrowExceptionWhenDocumentCustomerIsEmpty () {
        LoanDomainException exception = assertThrows(LoanDomainException.class, () -> {
            Loan.createRequestLoan (null, BigDecimal.valueOf(100), LocalDate.now().plusMonths(2));
        });

        assertEquals("Document must be mandatory", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenAmountIsNull () {
        LoanDomainException exception = assertThrows(LoanDomainException.class, () -> {
            Loan.createRequestLoan ("1011010111", null, LocalDate.now().plusMonths(2));
        });

        assertEquals("Amount must be mandatory", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenAmountIsNegative () {
        LoanDomainException exception = assertThrows(LoanDomainException.class, () -> {
            Loan.createRequestLoan ("1011010111", BigDecimal.valueOf(-100), LocalDate.now().plusMonths(2));
        });

        assertEquals("Amount must be positive", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenPeriodIsNull () {
        LoanDomainException exception = assertThrows(LoanDomainException.class, () -> {
            Loan.createRequestLoan ("1011010111", BigDecimal.valueOf(100), null);
        });

        assertEquals("Period must be mandatory", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenPeriodIsLowerThanNow () {
        LoanDomainException exception = assertThrows(LoanDomainException.class, () -> {
            Loan.createRequestLoan ("1011010111", BigDecimal.valueOf(100), LocalDate.now().minusMonths(2));
        });

        assertEquals("Period must be greater than now", exception.getMessage());
    }

    @Test
    void shouldReturnLoanWithSuccessful () {
        Loan domain = Loan.createRequestLoan ("1011010111", BigDecimal.valueOf(100), LocalDate.now().plusMonths(2));;

        assertNotNull(domain);
        assertInstanceOf(Loan.class, domain);

        assertNotNull(domain.getId().getValue());
        assertEquals(LoanStatus.PENDING, domain.getLoanStatus());
    }

}
