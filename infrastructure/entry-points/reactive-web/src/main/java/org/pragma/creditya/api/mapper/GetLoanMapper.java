package org.pragma.creditya.api.mapper;

import org.pragma.creditya.api.dto.request.GetLoanRequest;
import org.pragma.creditya.usecase.query.handler.loan.GetLoanFilter;
import org.pragma.creditya.usecase.query.Pagination;

public class GetLoanMapper {

    public static GetLoanFilter toQuery (GetLoanRequest request) {
        return new GetLoanFilter(
                request.document(),
                request.status(),
                new Pagination(request.page(), request.size())
        );
    }
}
