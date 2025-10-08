package org.pragma.creditya.usecase.outbox.payload;

import java.math.BigDecimal;

public record DebtPayload (String loanId, BigDecimal debt){
}
