package org.pragma.creditya.model.loan.exception;

import org.pragma.creditya.model.shared.domain.exception.DomainException;

public class DocumentDoestExistDomainException extends DomainException {
    public DocumentDoestExistDomainException(String message) {
        super(message);
    }
}
