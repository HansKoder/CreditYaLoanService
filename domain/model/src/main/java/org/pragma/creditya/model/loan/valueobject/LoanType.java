package org.pragma.creditya.model.loan.valueobject;

import org.pragma.creditya.model.loan.exception.LoanDomainException;

public record LoanType (Long code) {
    public LoanType {
        if (code == null)
            throw new LoanDomainException("Loan Type must be mandatory");
    }
}
