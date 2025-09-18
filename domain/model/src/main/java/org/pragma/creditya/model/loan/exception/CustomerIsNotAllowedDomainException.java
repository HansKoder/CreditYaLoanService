package org.pragma.creditya.model.loan.exception;

public class CustomerIsNotAllowedDomainException extends LoanDomainException {
    public CustomerIsNotAllowedDomainException(String message) {
        super(message);
    }
}
