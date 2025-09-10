package org.pragma.creditya.model.loanread.query;

import lombok.Builder;

@Builder
public record LoanQuery (String document, String status, Pagination pagination) {
}
