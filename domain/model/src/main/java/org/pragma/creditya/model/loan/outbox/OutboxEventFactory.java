package org.pragma.creditya.model.loan.outbox;

import org.pragma.creditya.model.customer.entity.Customer;
import org.pragma.creditya.model.loan.Loan;

public class OutboxEventFactory {

    public static NotifyDecisionEvent notifyDecision (Loan loan, Customer customer, String reason) {
        return new NotifyDecisionEvent(
                loan.getId().getValue().toString(),
                customer.getEmail(),
                customer.getName(),
                loan.getLoanStatus().name(),
                reason
        );
    }

}
