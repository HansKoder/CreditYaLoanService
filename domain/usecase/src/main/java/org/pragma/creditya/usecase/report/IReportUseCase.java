package org.pragma.creditya.usecase.report;

import org.pragma.creditya.model.loan.event.LoanApprovalStatisticsUpdatedEvent;
import reactor.core.publisher.Mono;

public interface IReportUseCase {
    Mono<Void> updateReport (LoanApprovalStatisticsUpdatedEvent event);
}
