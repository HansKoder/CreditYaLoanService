package org.pragma.creditya.model.customer.exception;

import org.pragma.creditya.model.loan.exception.LoanDomainException;

public class CustomerIsNotIdentifiedDomainException extends LoanDomainException {
    public CustomerIsNotIdentifiedDomainException(String message) {
        super(message);
    }
}
