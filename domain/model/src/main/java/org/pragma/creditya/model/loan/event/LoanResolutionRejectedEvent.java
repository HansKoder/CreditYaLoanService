package org.pragma.creditya.model.loan.event;

import lombok.Getter;
import lombok.Setter;
import org.pragma.creditya.model.loan.valueobject.LoanStatus;

import java.util.UUID;

@Setter
@Getter
public class LoanResolutionRejectedEvent extends LoanEvent{
    private LoanStatus status;
    private String reason;
    private String rejectedBy;

    public LoanResolutionRejectedEvent() {
    }

    public LoanResolutionRejectedEvent(RejectedBuilder builder) {
        super.setId(builder.eventId);
        super.setAggregateId(builder.aggregateId);
        super.setEventType(builder.eventType);
        super.setAggregateType(builder.aggregateType);
        super.setPayload(builder.payload);

        this.status = builder.status;
        this.reason = builder.reason;
        this.rejectedBy = builder.rejectedBy;
    }

    public static final class RejectedBuilder {
        private UUID eventId;
        private UUID aggregateId;
        private EventType eventType;
        private AggregateType aggregateType;

        private String reason;
        private LoanStatus status;
        private String rejectedBy;

        private LoanEventPayload payload;

        private RejectedBuilder() {}

        public static RejectedBuilder aRejectedEvent() {
            return new RejectedBuilder();
        }

        // Event sourcing fields
        public RejectedBuilder eventId(UUID value) {
            this.eventId = value;
            return this;
        }

        public RejectedBuilder aggregateId(UUID aggregateId) {
            this.aggregateId = aggregateId;
            return this;
        }

        public RejectedBuilder eventType(EventType eventType) {
            this.eventType = eventType;
            return this;
        }

        public RejectedBuilder aggregateType(AggregateType value) {
            this.aggregateType = value;
            return this;
        }

        // Rejected Event
        public RejectedBuilder reason(String reason) {
            this.reason = reason;
            return this;
        }

        public RejectedBuilder status(LoanStatus status) {
            this.status = status;
            return this;
        }

        public RejectedBuilder rejectedBy(String rejectedBy) {
            this.rejectedBy = rejectedBy;
            return this;
        }

        public RejectedBuilder payload(LoanEventPayload value) {
            this.payload = value;
            return this;
        }

        public LoanResolutionRejectedEvent build() {
            return new LoanResolutionRejectedEvent(this);
        }
    }
}
