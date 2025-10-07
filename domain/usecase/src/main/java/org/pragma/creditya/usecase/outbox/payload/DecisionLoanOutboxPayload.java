package org.pragma.creditya.usecase.outbox.payload;


import java.math.BigDecimal;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Builder
public class DecisionLoanOutboxPayload implements OutboxPayload{

    private BigDecimal customerSalary;
    private String currentLoanId;
    private BigDecimal currentLoanAmount;
    private List<DebtPayload> debts;

}
