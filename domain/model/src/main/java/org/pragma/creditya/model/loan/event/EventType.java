package org.pragma.creditya.model.loan.event;

public enum EventType {
    LOAN_SUBMITTED(ApplicationSubmittedEvent.class),
    LOAN_APPROVED(ApplicationApprovedEvent.class),
    LOAN_REJECTED(ApplicationRejectedEvent.class);

    private final Class<? extends LoanEventPayload> eventClass;

    EventType(Class<? extends LoanEventPayload> eventClass) {
        this.eventClass = eventClass;
    }

    public Class<? extends LoanEventPayload> getEventClass() {
        return eventClass;
    }

    public static EventType fromClass(Class<? extends LoanEventPayload> clazz) {
        for (EventType type : values()) {
            if (type.eventClass.equals(clazz)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No EventType registered for class " + clazz);
    }
}

