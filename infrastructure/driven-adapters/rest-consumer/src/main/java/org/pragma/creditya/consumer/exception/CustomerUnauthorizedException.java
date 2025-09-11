package org.pragma.creditya.consumer.exception;

import org.pragma.creditya.infracommon.exception.InfrastructureException;

public class CustomerUnauthorizedException extends InfrastructureException {
    public CustomerUnauthorizedException(String message) {
        super(message);
    }
}
