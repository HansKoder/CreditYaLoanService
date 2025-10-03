package org.pragma.creditya.usecase.query.exception;

import org.pragma.creditya.model.shared.domain.exception.DomainException;

public class PaginationDomainException extends DomainException {
    public PaginationDomainException(String message) {
        super(message);
    }
}
