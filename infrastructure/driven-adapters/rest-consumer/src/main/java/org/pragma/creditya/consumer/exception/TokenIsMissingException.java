package org.pragma.creditya.consumer.exception;

import org.pragma.creditya.consumer.InfrastructureException;

public class TokenIsMissingException extends InfrastructureException {
    public TokenIsMissingException(String message) {
        super(message);
    }
}
