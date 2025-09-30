package org.pragma.creditya.model.loan.event;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Setter
@Getter
public class LoanResolutionApprovedEvent extends LoanEvent{
    private String status;
    private String reason;
    private String approvedBy;

    public LoanResolutionApprovedEvent() {}

    public LoanResolutionApprovedEvent(ApprovedBuilder builder) {
        super.setAggregateId(builder.aggregateId);
        super.setEventType(builder.eventType);
        super.setAggregateType(builder.aggregateType);

        this.status = builder.status;
        this.reason = builder.reason;
        this.approvedBy = builder.approvedBy;
    }

    public static final class ApprovedBuilder {
        private UUID aggregateId;
        private EventType eventType;
        private AggregateType aggregateType;

        private String approvedBy;
        private String status;
        private String reason;

        private ApprovedBuilder() {
        }

        public static ApprovedBuilder anApprovedEvent() {
            return new ApprovedBuilder();
        }

        public ApprovedBuilder approvedBy(String approvedBy) {
            this.approvedBy = approvedBy;
            return this;
        }

        public ApprovedBuilder status(String status) {
            this.status = status;
            return this;
        }

        public ApprovedBuilder reason(String reason) {
            this.reason = reason;
            return this;
        }

        public ApprovedBuilder aggregateId(UUID aggregateId) {
            this.aggregateId = aggregateId;
            return this;
        }

        public ApprovedBuilder eventType(EventType value) {
            this.eventType = value;
            return this;
        }

        public ApprovedBuilder aggregateType(AggregateType value) {
            this.aggregateType = value;
            return this;
        }

        public LoanResolutionApprovedEvent build() {
            return new LoanResolutionApprovedEvent(this);
        }
    }
}
