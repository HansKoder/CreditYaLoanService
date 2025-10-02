package org.pragma.creditya.model.loan.valueobject;

import java.time.LocalDateTime;

public record Resolution (
        String decideBy,
        String reason,
        LocalDateTime decideAt
) { }
