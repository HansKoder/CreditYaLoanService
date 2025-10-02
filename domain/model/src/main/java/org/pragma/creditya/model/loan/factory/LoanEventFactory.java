package org.pragma.creditya.model.loan.factory;

import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loan.event.*;
import org.pragma.creditya.model.loantype.valueobject.ResolutionType;

public class LoanEventFactory {

    public static LoanApplicationSubmittedEvent submittedEvent (Loan domain, ResolutionType resolutionType) {
        return LoanApplicationSubmittedEvent.SubmittedBuilder.aSubmittedEvent()
                .aggregateId(domain.getId().getValue())
                .aggregateType(AggregateType.AGGREGATE_LOAN)
                .eventType(EventType.LOAN_SUBMITTED)
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
                .approvedBy(domain.getResolution().decideBy())
                .status(domain.getLoanStatus())
                .reason(domain.getResolution().reason())
                .build();
    }

    public static LoanResolutionRejectedEvent rejectedEvent (Loan domain) {
        return LoanResolutionRejectedEvent.RejectedBuilder.aRejectedEvent()
                .aggregateId(domain.getId().getValue())
                .aggregateType(AggregateType.AGGREGATE_LOAN)
                .eventType(EventType.LOAN_REJECTED)
                .rejectedBy(domain.getResolution().decideBy())
                .status(domain.getLoanStatus())
                .reason(domain.getResolution().reason())
                .build();
    }

}
