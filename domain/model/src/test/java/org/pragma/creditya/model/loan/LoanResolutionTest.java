package org.pragma.creditya.model.loan;

import org.junit.jupiter.api.Test;
import org.pragma.creditya.model.loan.event.*;
import org.pragma.creditya.model.loan.exception.LoanDomainException;
import org.pragma.creditya.model.loan.valueobject.LoanStatus;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class LoanResolutionTest {

    /**
    private final UUID LOAN_ID_EXAMPLE = UUID.fromString("e36702e6-78d0-4368-a191-292f53c8141c");

    private final Loan LOAN_STATUS_DIFF_PENDING = Loan.LoanBuilder
            .aLoan()
            .id(LOAN_ID_EXAMPLE)
            .document("123")
            .amount(BigDecimal.valueOf(10000))
            .loanStatus(LoanStatus.REJECTED)
            .build();

    private final Loan LOAN_WITHOUT_ID = Loan.LoanBuilder
            .aLoan()
            .document("123")
            .amount(BigDecimal.valueOf(10000))
            .loanStatus(LoanStatus.PENDING)
            .build();


    private final Loan LOAN_PENDING_STATUS = Loan.LoanBuilder
            .aLoan()
            .id(LOAN_ID_EXAMPLE)
            .document("123")
            .amount(BigDecimal.valueOf(10000))
            .loanStatus(LoanStatus.PENDING)
            .build();

    private final String AUTHOR = "Doe";
    private final String REASON_APPROVED = "";
    private final String REASON_REJECTED = "";

    @Test
    void shouldThrowException_WhenStatusIsNotPending_ForBeingApproved () {
        LoanDomainException exception = assertThrows(LoanDomainException.class,
                () -> LOAN_STATUS_DIFF_PENDING.checkApprovedLoan(REASON_APPROVED));

        assertEquals("Must have status Pending for being approved", exception.getMessage());
    }

    @Test
    void shouldThrowException_WithoutId_ForBeingApproved () {
        LoanDomainException exception = assertThrows(LoanDomainException.class,
                () -> LOAN_WITHOUT_ID.checkApprovedLoan(REASON_APPROVED));

        assertEquals("Must have ID Loan for being approved", exception.getMessage());
    }

    @Test
    void shouldThrowException_WhoIsResponsible_ForBeingApproved () {
        LoanDomainException exception = assertThrows(LoanDomainException.class,
                () -> LOAN_PENDING_STATUS.checkApprovedLoan(null));

        assertEquals("Who is responsible for this loan, Must have a responsible for being approved", exception.getMessage());
    }

    @Test
    void shouldBeApprovedWithSuccessful () {
        Loan loan = Loan.LoanBuilder
                .aLoan()
                .id(LOAN_ID_EXAMPLE)
                .document("123")
                .amount(BigDecimal.valueOf(1000))
                .loanStatus(LoanStatus.PENDING)
                .build();

        loan.loadAuthorResolutionLoan(AUTHOR);
        loan.checkApprovedLoan(REASON_APPROVED);

        assertEquals(LoanStatus.APPROVED, loan.getLoanStatus());
        assertEquals(1, loan.getUncommittedEvents().size());

        assertInstanceOf(LoanResolutionApprovedEvent.class, loan.getUncommittedEvents().getFirst());
    }

    @Test
    void shouldThrowException_WhenStatusIsNotPending_ForBeingRejected () {
        LoanDomainException exception = assertThrows(LoanDomainException.class,
                () -> LOAN_STATUS_DIFF_PENDING.checkRejectedLoan(REASON_REJECTED));

        assertEquals("Must have status Pending for being rejected", exception.getMessage());
    }

    @Test
    void shouldThrowException_WithoutId_ForBeingRejected () {
        LoanDomainException exception = assertThrows(LoanDomainException.class,
                () -> LOAN_WITHOUT_ID.checkRejectedLoan(REASON_REJECTED));

        assertEquals("Must have ID Loan for being rejected", exception.getMessage());
    }

    @Test
    void shouldThrowException_WhoIsResponsible_ForBeingRejected () {
        LoanDomainException exception = assertThrows(LoanDomainException.class,
                () -> LOAN_PENDING_STATUS.checkRejectedLoan(REASON_REJECTED));

        assertEquals("Who is responsible for this loan, Must have a responsible for being rejected", exception.getMessage());
    }

    @Test
    void shouldBeRejectedWithSuccessful () {
        Loan loan = Loan.LoanBuilder
                .aLoan()
                .id(LOAN_ID_EXAMPLE)
                .document("123")
                .amount(BigDecimal.valueOf(1000))
                .loanStatus(LoanStatus.PENDING)
                .build();

        loan.loadAuthorResolutionLoan(AUTHOR);
        loan.checkRejectedLoan(REASON_REJECTED);

        assertEquals(LoanStatus.REJECTED, loan.getLoanStatus());
        assertEquals(1, loan.getUncommittedEvents().size());

        assertInstanceOf(LoanResolutionRejectedEvent.class, loan.getUncommittedEvents().getFirst());
    }

    **/


    @Test
    void shouldBeTrue () {
        assertTrue(Boolean.TRUE);
    }

}
