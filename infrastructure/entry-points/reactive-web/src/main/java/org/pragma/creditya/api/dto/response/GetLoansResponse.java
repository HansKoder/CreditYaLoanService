package org.pragma.creditya.api.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record GetLoansResponse (
        UUID loanId,
        BigDecimal amount,
        String document
) {
}
