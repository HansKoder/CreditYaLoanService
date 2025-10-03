package org.pragma.creditya.usecase.query.handler.loan;

import org.pragma.creditya.usecase.query.Pagination;

public record GetLoanFilter(String document, String status, Pagination pagination) {
}
