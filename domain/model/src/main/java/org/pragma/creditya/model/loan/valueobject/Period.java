package org.pragma.creditya.model.loan.valueobject;

import org.pragma.creditya.model.loan.exception.LoanDomainException;

public record Period(int year, int month) {
    public Period {
        validatePeriod(year, month);
    }

    private void validatePeriod (int year, int month) {
        if (year < 0)
            throw new LoanDomainException("Year must be greater or equal to zero");

        if (month < 0)
            throw new LoanDomainException("Month must be greater or equal to zero");

        if (year == 0 && month == 0)
            throw new LoanDomainException("Must be scheduled any period of payment");
    }

    public int calculateTotalMonths() {
        return (year * 12) + month;
    }
}
