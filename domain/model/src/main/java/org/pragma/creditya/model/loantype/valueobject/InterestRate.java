package org.pragma.creditya.model.loantype.valueobject;

import org.pragma.creditya.model.loantype.exception.LoanTypeDomainException;

public record InterestRate (Double value) {
    public InterestRate {
        if (value == null)
            throw new LoanTypeDomainException("Interest Rate must be mandatory");

        if (value < 0L)
            throw new LoanTypeDomainException("Interest Rate must be positive");
    }
}
