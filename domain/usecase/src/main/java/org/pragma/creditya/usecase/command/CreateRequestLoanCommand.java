package org.pragma.creditya.usecase.command;

import java.math.BigDecimal;

public record CreateRequestLoanCommand(
        String document,
        BigDecimal amount,
        Long loanTypeId,
        int year,
        int month
) {
}
