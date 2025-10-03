package org.pragma.creditya.usecase.query.handler.loan;

import org.pragma.creditya.usecase.query.handler.loan.dto.LoanSummaryDTO;
import reactor.core.publisher.Flux;

public interface ILoanHandler {
    Flux<LoanSummaryDTO> getLoan (GetLoanQuery query);
}
