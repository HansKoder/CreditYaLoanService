package org.pragma.creditya.consumer.exception;

import org.pragma.creditya.infracommon.exception.InfrastructureException;

public class CustomerServiceUnavailableException extends InfrastructureException {
    public CustomerServiceUnavailableException(String message) {
        super(message);
    }
}
