package org.pragma.creditya.outbox.payload;


import java.math.BigDecimal;
import java.util.List;

/***
 export interface DecisionLoanCommand {
 customerSalary: number,
 currentLoanId: string,
 currentLoanAmount: number,
 debts: DebtCommand[]
 }

 */

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
