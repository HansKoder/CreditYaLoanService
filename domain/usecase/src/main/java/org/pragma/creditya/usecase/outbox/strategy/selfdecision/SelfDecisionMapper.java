package org.pragma.creditya.usecase.outbox.strategy.selfdecision;

import org.pragma.creditya.model.customer.entity.Customer;
import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.usecase.query.handler.loan.dto.LoanSummaryDTO;
import org.pragma.creditya.usecase.outbox.payload.DebtPayload;
import org.pragma.creditya.usecase.outbox.payload.DecisionLoanOutboxPayload;

import java.util.List;

public class SelfDecisionMapper {

    public static DecisionLoanOutboxPayload toPayload (Loan domain, List<LoanSummaryDTO> debts, Customer customer) {
        return DecisionLoanOutboxPayload
                .builder()
                .currentLoanId(domain.getId().getValue().toString())
                .currentLoanAmount(domain.getMonthlyDebt().amount())
                .customerSalary(customer.getBaseSalary().amount())
                .debts(toPayload(debts))
                .build();
    }

    private static List<DebtPayload> toPayload (List<LoanSummaryDTO> debts) {
        return debts.stream()
                .map(d -> new DebtPayload(d.getLoanId().toString(), d.getTotalMonthlyDebt()))
                .toList();
    }

}
