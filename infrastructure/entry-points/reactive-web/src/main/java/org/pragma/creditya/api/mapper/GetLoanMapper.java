package org.pragma.creditya.api.mapper;

import org.pragma.creditya.api.dto.request.GetLoanRequest;
import org.pragma.creditya.model.loanread.query.LoanQuery;
import org.pragma.creditya.model.loanread.query.Pagination;

public class GetLoanMapper {

    public static LoanQuery toQuery (GetLoanRequest request) {
        return LoanQuery.builder()
                .document(request.document())
                .pagination(new Pagination(request.page(), request.size()))
                .status(request.status())
                .build();
    }
}
