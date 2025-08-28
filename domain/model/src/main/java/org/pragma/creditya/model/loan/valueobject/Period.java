package org.pragma.creditya.model.loan.valueobject;

import org.pragma.creditya.model.loan.exception.LoanDomainException;

import java.time.LocalDate;

public record Period(LocalDate period) {
    public Period {
        if (period == null)
            throw new LoanDomainException("Period must be mandatory");

        if (!period.isAfter(LocalDate.now()))
            throw new LoanDomainException("Period must be greater than now");
    }
}
