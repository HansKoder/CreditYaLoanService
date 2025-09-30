package org.pragma.creditya.model.loan.factory;

import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loan.event.*;

public class LoanEventFactory {

    public static LoanApplicationSubmittedEvent submittedEvent (Loan domain) {
        return LoanApplicationSubmittedEvent.SubmittedBuilder.aSubmittedEvent()
                .aggregateId(domain.getId().getValue())
                .aggregateType(AggregateType.AGGREGATE_LOAN)
                .eventType(EventType.LOAN_SUBMITTED)
                .status(domain.getLoanStatus().name())
                .amount(domain.getAmount().amount())
                .typeLoan(domain.getLoanTypeCode().code())
                .period(domain.getPeriod().calculateTotalMonths())
                .totalMonthlyDebt(domain.getTotalMonthlyDebt().amount())
                .document(domain.getDocument().value())
                .build();
    }

    public static LoanResolutionApprovedEvent approvedEvent (Loan domain) {
        return LoanResolutionApprovedEvent.ApprovedBuilder.anApprovedEvent()
                .aggregateId(domain.getId().getValue())
                .aggregateType(AggregateType.AGGREGATE_LOAN)
                .eventType(EventType.LOAN_APPROVED)
                .approvedBy(domain.getResponsible())
                .status(domain.getLoanStatus().name())
                .reason(domain.getReason())
                .build();
    }

    public static LoanResolutionRejectedEvent rejectedEvent (Loan domain) {
        return LoanResolutionRejectedEvent.RejectedBuilder.aRejectedEvent()
                .aggregateId(domain.getId().getValue())
                .aggregateType(AggregateType.AGGREGATE_LOAN)
                .eventType(EventType.LOAN_REJECTED)
                .rejectedBy(domain.getResponsible())
                .status(domain.getLoanStatus().name())
                .reason(domain.getReason())
                .build();
    }

}
