package org.pragma.creditya.model.loan.event;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class LoanResolutionRejectedEvent extends LoanEvent{
    private String status;
    private String reason;
    private String rejectedBy;

    public LoanResolutionRejectedEvent() {
    }

    public LoanResolutionRejectedEvent(RejectedBuilder builder) {
        super.setAggregateId(builder.aggregateId);
        super.setEventType(builder.eventType);
        super.setAggregateType(builder.aggregateType);

        this.status = builder.status;
        this.reason = builder.reason;
        this.rejectedBy = builder.rejectedBy;
    }

    public static final class RejectedBuilder {
        private UUID aggregateId;
        private EventType eventType;
        private AggregateType aggregateType;

        private String reason;
        private String status;
        private String rejectedBy;

        private RejectedBuilder() {}

        public static RejectedBuilder aRejectedEvent() {
            return new RejectedBuilder();
        }

        public RejectedBuilder reason(String reason) {
            this.reason = reason;
            return this;
        }

        public RejectedBuilder status(String status) {
            this.status = status;
            return this;
        }

        public RejectedBuilder rejectedBy(String rejectedBy) {
            this.rejectedBy = rejectedBy;
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

        public LoanResolutionRejectedEvent build() {
            return new LoanResolutionRejectedEvent(this);
        }
    }
}
