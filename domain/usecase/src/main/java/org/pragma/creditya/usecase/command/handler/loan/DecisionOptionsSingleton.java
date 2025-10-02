package org.pragma.creditya.usecase.command.handler.loan;

import org.pragma.creditya.model.loan.valueobject.LoanStatus;

import java.util.Set;

public final class DecisionOptionsSingleton {

    private static final Set<LoanStatus> DECISION_OPTIONS;

    static {
        DECISION_OPTIONS = Set.of(LoanStatus.APPROVED, LoanStatus.REJECTED);
    }

    private DecisionOptionsSingleton() {
    }

    public static Set<LoanStatus> getInstance() {
        return DECISION_OPTIONS;
    }

}
