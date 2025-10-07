package org.pragma.creditya.consumer.machine.exception;

import org.pragma.creditya.infracommon.exception.InfrastructureException;

public class MachineUnauthorizedException extends InfrastructureException {
    public MachineUnauthorizedException(String message) {
        super(message);
    }
}
