package org.pragma.creditya.model.customer.exception;

import org.pragma.creditya.model.shared.domain.exception.DomainException;

public class CustomerDomainException extends DomainException {
    public CustomerDomainException(String message) {
        super(message);
    }
}
