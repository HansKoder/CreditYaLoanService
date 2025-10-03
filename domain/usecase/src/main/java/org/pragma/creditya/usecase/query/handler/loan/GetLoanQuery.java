package org.pragma.creditya.usecase.query.handler.loan;

import lombok.Builder;
import org.pragma.creditya.usecase.query.Pagination;

@Builder
public record GetLoanQuery(String document, String status, Pagination pagination) {
}
