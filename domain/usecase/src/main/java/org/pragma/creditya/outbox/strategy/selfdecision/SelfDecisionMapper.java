package org.pragma.creditya.outbox.strategy.selfdecision;

import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loan.entity.CustomerRead;
import org.pragma.creditya.model.loanread.LoanRead;
import org.pragma.creditya.outbox.payload.DebtPayload;
import org.pragma.creditya.outbox.payload.DecisionLoanOutboxPayload;

import java.util.List;

public class SelfDecisionMapper {

    public static DecisionLoanOutboxPayload toPayload (Loan domain, List<LoanRead> debts, CustomerRead customer) {
        return DecisionLoanOutboxPayload
                .builder()
                .currentLoanId(domain.getId().getValue().toString())
                .currentLoanAmount(domain.getAmount().amount())
                .customerSalary(customer.getBaseSalary())
                .debts(toPayload(debts))
                .build();
    }

    private static List<DebtPayload> toPayload (List<LoanRead> debts) {
        return debts.stream()
                .map(d -> new DebtPayload(d.getLoanId().toString(), d.getTotalMonthlyDebt()))
                .toList();
    }

}
