package org.pragma.creditya.model.loantype.valueobject;

import org.pragma.creditya.model.loantype.exception.LoanTypeDomainException;

public record Auto (Boolean value) {
    public Auto {
        if (value == null)
            throw new LoanTypeDomainException("Auto Decision must be mandatory");
    }
}
