package org.pragma.creditya.model.loan;

import org.junit.jupiter.api.Test;
import org.pragma.creditya.model.loan.entity.CustomerRead;
import org.pragma.creditya.model.loan.event.LoanApplicationSubmittedEvent;
import org.pragma.creditya.model.loan.event.LoanEvent;
import org.pragma.creditya.model.loan.event.LoanResolutionApprovedEvent;
import org.pragma.creditya.model.loan.event.LoanResolutionRejectedEvent;
import org.pragma.creditya.model.loan.exception.AmountLoanIsNotEnoughDomainException;
import org.pragma.creditya.model.loan.exception.LoanDomainException;
import org.pragma.creditya.model.loan.valueobject.LoanStatus;
import org.pragma.creditya.model.loantype.LoanType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class LoanEventStoringTest {

    // Dummy event mock unknowing event
    static class UnknownLoanEvent extends LoanEvent {
        public UnknownLoanEvent() {
        }
    }

    @Test
    void shouldThrowException_WhenEventTypeIsUnknown() {
        LoanEvent unknownEvent = new UnknownLoanEvent();
        List<LoanEvent> events = List.of(unknownEvent);

        LoanDomainException exception = assertThrows(
                LoanDomainException.class,
                () -> Loan.rehydrate(events)
        );

        assertTrue(exception.getMessage().contains("Unknown event type"));
    }

    @Test
    void shouldThrowException_CannotRehydrateLoanWithoutEvents () {
        List<LoanEvent> events = List.of();

        LoanDomainException exception = assertThrows(
                LoanDomainException.class,
                () -> Loan.rehydrate(events)
        );

        assertTrue(exception.getMessage().contains("Cannot rehydrate Loan without events"));
    }

    @Test
    void shouldRehydrateLoan_FromApplicationSubmittedEvent() {
        UUID aggregateId = UUID.randomUUID();

        LoanApplicationSubmittedEvent submittedEvent =
                LoanApplicationSubmittedEvent.LoanBuilder.aLoanApplicationSubmitted()
                        .aggregateId(aggregateId)
                        .aggregateType("LOAN")
                        .eventType(LoanApplicationSubmittedEvent.class.getSimpleName())
                        .timestamp(Instant.now())
                        .document("123456789")
                        .status(LoanStatus.PENDING.name())
                        .amount(new BigDecimal("5000"))
                        .typeLoan(1L)
                        .period(12)
                        .totalMonthlyDebt(new BigDecimal("416.67"))
                        .build();

        Loan loan = Loan.rehydrate(List.of(submittedEvent));

        assertEquals(aggregateId, loan.getId().getValue());
        assertEquals(LoanStatus.PENDING, loan.getLoanStatus());
        assertEquals("123456789", loan.getDocument().value());
        assertEquals(new BigDecimal("5000"), loan.getAmount().amount());
        assertEquals(new BigDecimal("416.67"), loan.getTotalMonthlyDebt().amount());
    }

    @Test
    void shouldRehydrateLoan_FromApplicationAndApprovedEvent() {
        UUID aggregateId = UUID.randomUUID();

        LoanApplicationSubmittedEvent submittedEvent =
                LoanApplicationSubmittedEvent.LoanBuilder.aLoanApplicationSubmitted()
                        .aggregateId(aggregateId)
                        .aggregateType("LOAN")
                        .eventType(LoanApplicationSubmittedEvent.class.getSimpleName())
                        .timestamp(Instant.now())
                        .document("987654321")
                        .status(LoanStatus.PENDING.name())
                        .amount(new BigDecimal("10000"))
                        .typeLoan(2L)
                        .period(24)
                        .totalMonthlyDebt(new BigDecimal("416.67"))
                        .build();

        var approvedEvent = LoanResolutionApprovedEvent.LoanBuilder
                .aLoanResolutionApproved()
                .aggregateId(aggregateId)
                .approvedBy("manager-user")
                .reason("All checks passed")
                .build();

        Loan loan = Loan.rehydrate(List.of(submittedEvent, approvedEvent));

        assertEquals(aggregateId, loan.getId().getValue());
        assertEquals(LoanStatus.APPROVED, loan.getLoanStatus());
        assertEquals("manager-user", loan.getResponsible());
    }

    @Test
    void shouldRehydrateLoan_FromApplicationAndRejectedEvent() {
        UUID aggregateId = UUID.randomUUID();

        LoanApplicationSubmittedEvent submittedEvent =
                LoanApplicationSubmittedEvent.LoanBuilder.aLoanApplicationSubmitted()
                        .aggregateId(aggregateId)
                        .aggregateType("LOAN")
                        .eventType(LoanApplicationSubmittedEvent.class.getSimpleName())
                        .timestamp(Instant.now())
                        .document("987654321")
                        .status(LoanStatus.PENDING.name())
                        .amount(new BigDecimal("10000"))
                        .typeLoan(2L)
                        .period(24)
                        .totalMonthlyDebt(new BigDecimal("416.67"))
                        .build();

        var rejectedEvent = LoanResolutionRejectedEvent.LoanBuilder.aLoanResolutionRejected()
                .aggregateId(aggregateId)
                .rejectedBy("manager-user")
                .reason("All checks passed")
                .build();

        Loan loan = Loan.rehydrate(List.of(submittedEvent, rejectedEvent));

        assertEquals(aggregateId, loan.getId().getValue());
        assertEquals(LoanStatus.REJECTED, loan.getLoanStatus());
        assertEquals("manager-user", loan.getResponsible());
    }

}
