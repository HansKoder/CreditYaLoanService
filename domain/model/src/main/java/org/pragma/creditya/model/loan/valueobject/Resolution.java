package org.pragma.creditya.model.loan.valueobject;

public record Resolution (
        String by,
        String reason,
        LoanStatus decision
) { }
