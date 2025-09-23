package org.pragma.creditya.usecase.report;

import org.pragma.creditya.model.loan.event.LoanApprovalStatisticsUpdatedEvent;
import reactor.core.publisher.Mono;

public class ReportUseCase implements IReportUseCase{
    @Override
    public Mono<Void> updateReport(LoanApprovalStatisticsUpdatedEvent event) {
        return null;
    }
}
