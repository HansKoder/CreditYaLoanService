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
                .monthlyDebt(domain.getMonthlyDebt().amount())
                .build();
    }

    private static ApplicationApprovedEvent approvedEventPayload (Loan domain) {
        return ApplicationApprovedEvent.builder()
                .approvedBy(domain.getResolution().by())
                .status(domain.getLoanStatus())
                .reason(domain.getResolution().reason())
                .build();
    }

    private static ApplicationRejectedEvent rejectedEventPayload (Loan domain) {
        return ApplicationRejectedEvent.builder()
                .rejectedBy(domain.getResolution().by())
                .status(domain.getLoanStatus())
                .reason(domain.getResolution().reason())
                .build();
    }

    public static LoanApplicationSubmittedEvent submittedEvent (Loan domain, ResolutionType resolutionType) {
        var event = LoanApplicationSubmittedEvent.SubmittedBuilder.aSubmittedEvent()
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

        System.out.println("[domain.loan.factory] (submittedEvent) build a new event, response=[ submittedEvent:{"+ event + "} ]");

        return event;
    }

    public static LoanResolutionApprovedEvent approvedEvent (Loan domain) {
        return LoanResolutionApprovedEvent.ApprovedBuilder.anApprovedEvent()
                .aggregateId(domain.getId().getValue())
                .aggregateType(AggregateType.AGGREGATE_LOAN)
                .eventType(EventType.LOAN_APPROVED)
                .payload(approvedEventPayload(domain))
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
                .payload(rejectedEventPayload(domain))
                .rejectedBy(domain.getResolution().by())
                .status(domain.getLoanStatus())
                .reason(domain.getResolution().reason())
                .build();
    }

}
