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
    private BigDecimal amount;
    private Integer months;
    private String status;

    // calculate
    private BigDecimal totalMonthlyDebt;

    // customer
    private String document;
    private String name;
    private String email;
    private BigDecimal baseSalary;

    // loan type
    private Long typeLoan;
    private String typeLoanDescription;
    private Double interestRate;

    private Instant timestamp;

}
