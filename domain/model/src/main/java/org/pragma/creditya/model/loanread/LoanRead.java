package org.pragma.creditya.model.loanread;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoanRead {
    private UUID loanId;
    private String document;
    private BigDecimal amount;
}
