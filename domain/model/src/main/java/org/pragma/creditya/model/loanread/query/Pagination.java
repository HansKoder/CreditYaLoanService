package org.pragma.creditya.model.loanread.query;

import org.pragma.creditya.model.loanread.exception.PaginationDomainException;

public record Pagination (
        int page,
        int size
) {
    public Pagination {
        if (page < 0)
            throw new PaginationDomainException("The page the min value must be ZERO (O)");

        if (size < 1)
            throw new PaginationDomainException("The size the min value must be ONE (1)");
    }
}
