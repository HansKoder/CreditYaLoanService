package org.pragma.creditya.model.loan.event;

import org.pragma.creditya.model.shared.domain.event.DomainEvent;

public enum EventType {
    LOAN_SUBMITTED(ApplicationSubmittedEvent.class),
    LOAN_APPROVED(LoanResolutionApprovedEvent.class),
    LOAN_REJECTED(LoanResolutionRejectedEvent.class);

    private final Class<? extends DomainEvent<?>> eventClass;

    EventType(Class<? extends DomainEvent<?>> eventClass) {
        this.eventClass = eventClass;
    }

    public Class<? extends DomainEvent<?>> getEventClass() {
        return eventClass;
    }

    public static EventType fromClass(Class<? extends DomainEvent<?>> clazz) {
        for (EventType type : values()) {
            if (type.eventClass.equals(clazz)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No EventType registered for class " + clazz);
    }
}

