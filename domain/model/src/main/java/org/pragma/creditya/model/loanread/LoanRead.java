package org.pragma.creditya.model.loanread;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class LoanRead {
    private String id;
    private UUID loanId;
    private String document;
    private BigDecimal amount;
    private Long typeLoan;
    private Integer months;
    private String status;
    private BigDecimal totalMonthlyDebt;
    private Instant timestamp;

}
