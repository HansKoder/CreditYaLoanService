package org.pragma.creditya.api.mapper;

import org.pragma.creditya.api.dto.request.GetLoanRequest;
import org.pragma.creditya.usecase.query.handler.loan.GetLoanQuery;
import org.pragma.creditya.usecase.query.Pagination;

public class GetLoanMapper {

    public static GetLoanQuery toQuery (GetLoanRequest request) {
        return GetLoanQuery.builder()
                .document(request.document())
                .pagination(new Pagination(request.page(), request.size()))
                .status(request.status())
                .build();
    }
}
