package org.pragma.creditya.model.loan.exception;

import org.pragma.creditya.model.shared.domain.exception.DomainException;

public class DocumentNotFoundDomainException extends DomainException {
    public DocumentNotFoundDomainException(String message) {
        super(message);
    }
}
