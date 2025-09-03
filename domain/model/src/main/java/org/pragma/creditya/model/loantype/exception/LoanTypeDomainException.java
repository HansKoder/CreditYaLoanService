package org.pragma.creditya.model.loantype.exception;

import org.pragma.creditya.model.shared.domain.exception.DomainException;

public class LoanTypeDomainException extends DomainException {
    public LoanTypeDomainException(String message) {
        super(message);
    }
}
