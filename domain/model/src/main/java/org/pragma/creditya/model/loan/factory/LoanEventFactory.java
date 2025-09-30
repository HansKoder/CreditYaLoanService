package org.pragma.creditya.model.loan.factory;

import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loan.event.LoanApplicationSubmittedEvent;
import org.pragma.creditya.model.loan.event.LoanResolutionApprovedEvent;
import org.pragma.creditya.model.loan.event.LoanResolutionRejectedEvent;

import java.time.Instant;

public class LoanEventFactory {

    public static LoanApplicationSubmittedEvent submittedEvent (Loan domain) {
        return LoanApplicationSubmittedEvent.LoanBuilder.
                aLoanApplicationSubmitted()
                .aggregateId(domain.getId().getValue())
                .aggregateType(domain.getAGGREGATE_TYPE())
                .eventType(LoanApplicationSubmittedEvent.class.getSimpleName())
                .status(domain.getLoanStatus().name())
                .amount(domain.getAmount().amount())
                .typeLoan(domain.getLoanTypeCode().code())
                .period(domain.getPeriod().calculateTotalMonths())
                .totalMonthlyDebt(domain.getTotalMonthlyDebt().amount())
                .document(domain.getDocument().value())
                .build();
    }

    public static LoanResolutionApprovedEvent approvedEvent (Loan domain) {
        return LoanResolutionApprovedEvent.LoanBuilder.aLoanResolutionApproved()
                .aggregateId(domain.getId().getValue())
                .aggregateType(domain.getAGGREGATE_TYPE())
                .eventType(LoanResolutionApprovedEvent.class.getSimpleName())
                .approvedBy(domain.getResponsible())
                .status(domain.getLoanStatus().name())
                .reason(domain.getReason())
                .build();
    }

    public static LoanResolutionRejectedEvent rejectedEvent (Loan domain) {
        return LoanResolutionRejectedEvent.LoanBuilder.aLoanResolutionRejected()
                .aggregateId(domain.getId().getValue())
                .aggregateType(domain.getAGGREGATE_TYPE())
                .eventType(LoanResolutionRejectedEvent.class.getSimpleName())
                .rejectedBy(domain.getResponsible())
                .status(domain.getLoanStatus().name())
                .reason(domain.getReason())
                .build();
    }

}
