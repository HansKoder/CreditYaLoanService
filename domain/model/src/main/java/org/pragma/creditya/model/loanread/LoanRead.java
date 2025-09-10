package org.pragma.creditya.model.loanread;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record LoanRead (
        UUID loanId,
        String document,
        BigDecimal amount
) { }
