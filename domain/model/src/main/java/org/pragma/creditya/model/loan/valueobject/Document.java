package org.pragma.creditya.model.loan.valueobject;

import org.pragma.creditya.model.loan.exception.LoanDomainException;

public record Document(String value) {
    public Document {
        if (value == null || value.isBlank())
            throw new LoanDomainException("Document must be mandatory");
    }
}
