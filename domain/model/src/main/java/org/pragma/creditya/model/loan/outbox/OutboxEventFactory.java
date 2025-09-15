package org.pragma.creditya.model.loan.outbox;

import org.pragma.creditya.model.loan.Loan;

public class OutboxEventFactory {

    public static NotifyDecisionEvent notifyDecision (Loan loan) {
        return new NotifyDecisionEvent(
          loan.getId().getValue().toString(),

        );
    }

}
