package org.pragma.creditya.api.dto.request;

public record ResolutionApplicationLoanRequest (
        String loanId,
        String decision,
        String reason
) { }
