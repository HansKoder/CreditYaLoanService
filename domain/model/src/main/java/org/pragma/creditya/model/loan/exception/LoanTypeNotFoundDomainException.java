package org.pragma.creditya.model.loan.exception;

import org.pragma.creditya.model.shared.domain.exception.DomainException;

public class LoanTypeNotFoundDomainException extends DomainException {
    public LoanTypeNotFoundDomainException(String message) {
        super(message);
    }
}
