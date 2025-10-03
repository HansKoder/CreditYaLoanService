package org.pragma.creditya.model.loan;

import org.junit.jupiter.api.Test;
import org.pragma.creditya.model.loan.event.*;
import org.pragma.creditya.model.loan.exception.LoanDomainException;
import org.pragma.creditya.model.loan.valueobject.LoanStatus;

import java.math.BigDecimal;
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
                LoanApplicationSubmittedEvent.SubmittedBuilder.aSubmittedEvent()
                        .aggregateId(aggregateId)
                        .aggregateType(AggregateType.AGGREGATE_LOAN)
                        .eventType(EventType.LOAN_SUBMITTED)
                        .document("123456789")
                        .status(LoanStatus.PENDING)
                        .amount(new BigDecimal("5000"))
                        .typeLoan(1L)
                        .period(12)
                        .monthlyDebt(new BigDecimal("416.67"))
                        .build();

        Loan loan = Loan.rehydrate(List.of(submittedEvent));

        assertEquals(aggregateId, loan.getId().getValue());
        assertEquals(LoanStatus.PENDING, loan.getLoanStatus());
        assertEquals("123456789", loan.getDocument().getValue());
        assertEquals(new BigDecimal("5000"), loan.getAmount().amount());
        assertEquals(new BigDecimal("416.67"), loan.getMonthlyDebt().amount());
    }

    @Test
    void shouldRehydrateLoan_FromApplicationAndApprovedEvent() {
        UUID aggregateId = UUID.randomUUID();

        LoanApplicationSubmittedEvent submittedEvent =
                LoanApplicationSubmittedEvent.SubmittedBuilder.aSubmittedEvent()
                        .aggregateId(aggregateId)
                        .aggregateType(AggregateType.AGGREGATE_LOAN)
                        .eventType(EventType.LOAN_SUBMITTED)
                        .document("987654321")
                        .status(LoanStatus.PENDING)
                        .amount(new BigDecimal("10000"))
                        .typeLoan(2L)
                        .period(24)
                        .monthlyDebt(new BigDecimal("416.67"))
                        .build();

        var approvedEvent = LoanResolutionApprovedEvent.ApprovedBuilder.anApprovedEvent()
                .aggregateId(aggregateId)
                .approvedBy("manager-user")
                .reason("All checks passed")
                .build();

        Loan loan = Loan.rehydrate(List.of(submittedEvent, approvedEvent));

        assertEquals(aggregateId, loan.getId().getValue());
        assertEquals(LoanStatus.APPROVED, loan.getLoanStatus());
        assertEquals("manager-user", loan.getResolution().by());
    }

    @Test
    void shouldRehydrateLoan_FromApplicationAndRejectedEvent() {
        UUID aggregateId = UUID.randomUUID();

        LoanApplicationSubmittedEvent submittedEvent =
                LoanApplicationSubmittedEvent.SubmittedBuilder.aSubmittedEvent()
                        .aggregateId(aggregateId)
                        .aggregateType(AggregateType.AGGREGATE_LOAN)
                        .eventType(EventType.LOAN_SUBMITTED)
                        .document("987654321")
                        .status(LoanStatus.PENDING)
                        .amount(new BigDecimal("10000"))
                        .typeLoan(2L)
                        .period(24)
                        .monthlyDebt(new BigDecimal("416.67"))
                        .build();

        var rejectedEvent = LoanResolutionRejectedEvent.RejectedBuilder.aRejectedEvent()
                .aggregateId(aggregateId)
                .rejectedBy("manager-user")
                .reason("All checks passed")
                .build();

        Loan loan = Loan.rehydrate(List.of(submittedEvent, rejectedEvent));

        assertEquals(aggregateId, loan.getId().getValue());
        assertEquals(LoanStatus.REJECTED, loan.getLoanStatus());
        assertEquals("manager-user", loan.getResolution().by());
    }

}
