package org.pragma.creditya.model.loan.valueobject;

import java.time.LocalDateTime;

public record Resolution (
        String by,
        String reason,
        LoanStatus decision,
        LocalDateTime decidedAt
) { }
