package org.pragma.creditya.consumer.customer.exception;

import org.pragma.creditya.infracommon.exception.InfrastructureException;

public class CustomerBadRequestException extends InfrastructureException {
    public CustomerBadRequestException(String message) {
        super(message);
    }
}
