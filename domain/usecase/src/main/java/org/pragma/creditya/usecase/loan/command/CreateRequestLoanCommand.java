package org.pragma.creditya.usecase.loan.command;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateRequestLoanCommand(
        String document,
        BigDecimal amount,
        LocalDate period
) {
}
