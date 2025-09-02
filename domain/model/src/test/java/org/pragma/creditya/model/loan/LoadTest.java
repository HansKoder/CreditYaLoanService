package org.pragma.creditya.model.loan;

import org.junit.jupiter.api.Test;
import org.pragma.creditya.model.loan.exception.LoanDomainException;
import org.pragma.creditya.model.loan.valueobject.LoanStatus;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class LoadTest {

    @Test
    void shouldThrowExceptionWhenDocumentCustomerIsEmpty () {
        LoanDomainException exception = assertThrows(LoanDomainException.class, () -> {
            Loan.LoanBuilder
                    .aLoan()
                    .build();
        });

        assertEquals("Document must be mandatory", exception.getMessage());
    }


    @Test
    void shouldThrowExceptionWhenAmountIsNull () {
        LoanDomainException exception = assertThrows(LoanDomainException.class, () -> {
            Loan.LoanBuilder
                    .aLoan()
                    .document("123")
                    .build();
        });

        assertEquals("Amount must be mandatory", exception.getMessage());
    }


    @Test
    void shouldThrowExceptionWhenAmountIsNegative () {
        LoanDomainException exception = assertThrows(LoanDomainException.class, () -> {
            Loan.LoanBuilder
                    .aLoan()
                    .document("123")
                    .amount(BigDecimal.valueOf(-5))
                    .build();
        });

        assertEquals("Amount must be positive", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenYearIsNegative () {
        LoanDomainException exception = assertThrows(LoanDomainException.class, () -> {
            Loan.LoanBuilder
                    .aLoan()
                    .document("123")
                    .amount(BigDecimal.valueOf(10))
                    .period(-5, 6)
                    .build();
        });

        assertEquals("Year must be greater or equal to zero", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenLoanTypeIsNull () {
        LoanDomainException exception = assertThrows(LoanDomainException.class, () -> {
            Loan.LoanBuilder
                    .aLoan()
                    .document("123")
                    .amount(BigDecimal.valueOf(10))
                    .period(1, 6)
                    .loanType(null)
                    .build();
        });

        assertEquals("Loan Type must be mandatory", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenUUIDIsDefined () {
        Loan domain = Loan.LoanBuilder
                .aLoan()
                .id(UUID.fromString("e36702e6-78d0-4368-a191-292f53c8141c"))
                .document("123")
                .amount(BigDecimal.valueOf(10))
                .period(1, 6)
                .loanType(1L)
                .build();

        LoanDomainException exception = assertThrows(LoanDomainException.class, domain::createRequestLoan);

        assertEquals("Must be without ID", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenStatusIsInvalid () {
        Loan domain = Loan.LoanBuilder
                .aLoan()
                .document("123")
                .amount(BigDecimal.valueOf(10))
                .period(1, 6)
                .loanType(1L)
                .loanStatus(LoanStatus.APPROVED)
                .build();

        LoanDomainException exception = assertThrows(LoanDomainException.class, domain::createRequestLoan);

        assertEquals("Invalid status to create request", exception.getMessage());
    }


    @Test
    void shouldReturnLoanWithSuccessful () {
        Loan domain = Loan.LoanBuilder
                .aLoan()
                .document("123")
                .amount(BigDecimal.valueOf(10))
                .period(1, 6)
                .loanType(1L)
                .build();

        domain.createRequestLoan();

        assertNotNull(domain);
        assertInstanceOf(Loan.class, domain);

        assertNotNull(domain.getId());
        assertNull(domain.getId().getValue());
        assertEquals(LoanStatus.PENDING, domain.getLoanStatus());
        assertEquals(18, domain.getPeriod().calculateTotalMonths());
    }

}
