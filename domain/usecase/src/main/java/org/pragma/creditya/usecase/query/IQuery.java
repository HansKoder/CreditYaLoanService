package org.pragma.creditya.usecase.query;

import org.pragma.creditya.usecase.query.handler.loan.GetLoanFilter;
import org.pragma.creditya.usecase.query.handler.loan.dto.LoanSummaryDTO;
import reactor.core.publisher.Flux;

public interface IQuery {

    Flux<LoanSummaryDTO> getLoans(GetLoanFilter query);

}
