package org.pragma.creditya.consumer.customer.exception;

import org.pragma.creditya.infracommon.exception.InfrastructureException;

public class CustomerServiceException extends InfrastructureException {
    public CustomerServiceException(String message) {
        super(message);
    }
}
