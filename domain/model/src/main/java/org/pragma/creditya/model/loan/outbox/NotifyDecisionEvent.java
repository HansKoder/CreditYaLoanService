package org.pragma.creditya.model.loan.outbox;

public record NotifyDecisionEvent (
        String loanId,
        String applicantEmail,
        String applicantName,
        String decision,
        String reason
) implements LoanDetailEvent{
    @Override
    public String eventType() {
        return "NOTIFY_DECISION_LOAN";
    }

    @Override
    public String aggregate() {
        return "LOAN";
    }

    @Override
    public String aggregateId() {
        return loanId;
    }
}
