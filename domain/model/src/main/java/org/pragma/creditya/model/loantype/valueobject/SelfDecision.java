package org.pragma.creditya.model.loantype.valueobject;

import org.pragma.creditya.model.loantype.exception.LoanTypeDomainException;

public record SelfDecision(Boolean value) {
    public SelfDecision {
        if (value == null)
            throw new LoanTypeDomainException("Self Decision must be mandatory");
    }
}
