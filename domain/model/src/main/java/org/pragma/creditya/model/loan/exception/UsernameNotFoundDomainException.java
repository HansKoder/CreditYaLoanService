package org.pragma.creditya.model.loan.exception;

import org.pragma.creditya.model.shared.domain.exception.DomainException;

public class UsernameNotFoundDomainException  extends DomainException {
    public UsernameNotFoundDomainException(String message) {
        super(message);
    }
}
