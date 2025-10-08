package org.pragma.creditya.usecase.outbox.strategy.report;

import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.usecase.outbox.payload.ReportOutboxPayload;

public class ReportMapper {

    public static ReportOutboxPayload toPayload (Loan domain) {
        return ReportOutboxPayload.builder()
                .loanId(domain.getId().getValue().toString())
                .amount(domain.getAmount().amount())
                .build();
    }

}
