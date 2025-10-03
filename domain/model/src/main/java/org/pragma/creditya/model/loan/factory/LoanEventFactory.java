package org.pragma.creditya.model.loan.factory;

import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loan.event.*;
import org.pragma.creditya.model.loantype.valueobject.ResolutionType;

public class LoanEventFactory {

    private static ApplicationSubmittedEvent submittedEventPayload (Loan domain, ResolutionType resolutionType) {
        return ApplicationSubmittedEvent.builder()
                .status(domain.getLoanStatus())
                .amount(domain.getAmount().amount())
                .typeLoan(domain.getLoanTypeId().getValue())
                .period(domain.getPeriod().calculateTotalMonths())
                .document(domain.getDocument().getValue())
                .resolutionType(resolutionType)
                .build();
    }

    public static LoanApplicationSubmittedEvent submittedEvent (Loan domain, ResolutionType resolutionType) {
        return LoanApplicationSubmittedEvent.SubmittedBuilder.aSubmittedEvent()
                .aggregateId(domain.getId().getValue())
                .aggregateType(AggregateType.AGGREGATE_LOAN)
                .eventType(EventType.LOAN_SUBMITTED)
                .payload(submittedEventPayload(domain, resolutionType)) // Test Submitted using Payload
                .status(domain.getLoanStatus())
                .amount(domain.getAmount().amount())
                .typeLoan(domain.getLoanTypeId().getValue())
                .period(domain.getPeriod().calculateTotalMonths())
                .document(domain.getDocument().getValue())
                .resolutionType(resolutionType)
                .build();
    }

    public static LoanResolutionApprovedEvent approvedEvent (Loan domain) {
        return LoanResolutionApprovedEvent.ApprovedBuilder.anApprovedEvent()
                .aggregateId(domain.getId().getValue())
                .aggregateType(AggregateType.AGGREGATE_LOAN)
                .eventType(EventType.LOAN_APPROVED)
                .approvedBy(domain.getResolution().by())
                .status(domain.getLoanStatus())
                .reason(domain.getResolution().reason())
                .build();
    }

    public static LoanResolutionRejectedEvent rejectedEvent (Loan domain) {
        return LoanResolutionRejectedEvent.RejectedBuilder.aRejectedEvent()
                .aggregateId(domain.getId().getValue())
                .aggregateType(AggregateType.AGGREGATE_LOAN)
                .eventType(EventType.LOAN_REJECTED)
                .rejectedBy(domain.getResolution().by())
                .status(domain.getLoanStatus())
                .reason(domain.getResolution().reason())
                .build();
    }

}
