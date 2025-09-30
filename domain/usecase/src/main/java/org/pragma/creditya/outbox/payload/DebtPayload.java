package org.pragma.creditya.outbox.payload;

import java.math.BigDecimal;

public record DebtPayload (String loanId, BigDecimal price){
}
