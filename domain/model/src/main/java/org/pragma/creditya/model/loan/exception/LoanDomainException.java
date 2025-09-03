package org.pragma.creditya.model.loan.exception;

import org.pragma.creditya.model.shared.domain.exception.DomainException;

public class LoanDomainException extends DomainException {
    public LoanDomainException(String message) {
        super(message);
    }
}
