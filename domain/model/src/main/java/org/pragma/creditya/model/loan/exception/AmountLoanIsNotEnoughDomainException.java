package org.pragma.creditya.model.loan.exception;

import org.pragma.creditya.model.shared.domain.exception.DomainException;

public class AmountLoanIsNotEnoughDomainException extends DomainException {
    public AmountLoanIsNotEnoughDomainException(String message) {
        super(message);
    }
}
