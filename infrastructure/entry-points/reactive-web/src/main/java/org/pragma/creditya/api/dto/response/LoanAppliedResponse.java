package org.pragma.creditya.api.dto.response;

import java.math.BigDecimal;

public record LoanAppliedResponse(
        String loanId,
        String document,
        BigDecimal amount,
        Long loanTypeId,
        int year,
        int month,
        String status
) { }
