package org.pragma.creditya.model.shared.domain.model.valueobject;

import org.pragma.creditya.model.loan.exception.LoanDomainException;

import java.math.BigDecimal;

public record Amount(BigDecimal amount) {
    public Amount {
        if (amount == null)
            throw new LoanDomainException("Amount must be mandatory");

        if (!isGreaterThanZero(amount))
            throw new LoanDomainException("Amount must be positive");
    }

    private boolean isGreaterThanZero(BigDecimal amount) {
        return amount != null && amount.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isGreaterThan(BigDecimal other) {
        return amount != null && amount.compareTo(other) > 0;
    }

}
