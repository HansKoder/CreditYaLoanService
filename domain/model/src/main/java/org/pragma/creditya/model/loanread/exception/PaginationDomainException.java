package org.pragma.creditya.model.loanread.exception;

import org.pragma.creditya.model.shared.domain.exception.DomainException;

public class PaginationDomainException extends DomainException {
    public PaginationDomainException(String message) {
        super(message);
    }
}
