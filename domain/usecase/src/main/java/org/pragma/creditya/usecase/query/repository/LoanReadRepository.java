package org.pragma.creditya.usecase.query.repository;

import org.pragma.creditya.model.customer.valueobject.Document;
import org.pragma.creditya.usecase.query.handler.loan.GetLoanFilter;
import org.pragma.creditya.usecase.query.handler.loan.dto.LoanSummaryDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface LoanReadRepository {

    Mono<Void> saveLoanRead (LoanSummaryDTO read);
    Flux<LoanSummaryDTO> getLoan (GetLoanFilter query);
    Mono<LoanSummaryDTO> getLoanByAggregateId (UUID aggregateId);
    Flux<LoanSummaryDTO> getActiveDebts (Document document);

}
