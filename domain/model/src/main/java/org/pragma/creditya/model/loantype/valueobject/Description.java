package org.pragma.creditya.model.loantype.valueobject;

import org.pragma.creditya.model.loantype.exception.LoanTypeDomainException;

public record Description (String value) {
    public Description {
        if (value == null || value.isBlank())
            throw new LoanTypeDomainException("Description Loan Type must be mandatory");
    }
}
