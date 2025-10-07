package org.pragma.creditya.consumer.customer.exception;

import org.pragma.creditya.consumer.InfrastructureException;

public class CustomerNotFoundException extends InfrastructureException {
    public CustomerNotFoundException(String message) {
        super(message);
    }
}
