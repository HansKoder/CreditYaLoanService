package org.pragma.creditya.api.dto.request;

import java.math.BigDecimal;

public record CreateApplicationLoanRequest(
    String document,
    BigDecimal amount,
    Long loanTypeId,
    int year,
    int month
) { }
