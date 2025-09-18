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

    public LoanResolutionApprovedEvent() {
    }

    public LoanResolutionApprovedEvent(LoanBuilder loanBuilder) {
        // super(loanBuilder.aggregateId, loanBuilder.version, loanBuilder.timestamp, loanBuilder.eventType, loanBuilder.aggregateType);
        super.setAggregateId(loanBuilder.aggregateId);
        super.setEventType(loanBuilder.eventType);
        super.setVersion(1);
        super.setAggregateType(loanBuilder.aggregateType);
        super.setDestination(EventDestination.INTERNAL);

        this.status = loanBuilder.status;
        this.reason = loanBuilder.reason;
        this.approvedBy = loanBuilder.approvedBy;
    }

    public static final class LoanBuilder {
        private String approvedBy;
        private String status;
        private String reason;
        private UUID aggregateId;
        private int version;
        private Instant timestamp;
        private String eventType;
        private String aggregateType;

        private LoanBuilder() {
        }

        public static LoanBuilder aLoanResolutionApproved() {
            return new LoanBuilder();
        }

        public LoanBuilder approvedBy(String approvedBy) {
            this.approvedBy = approvedBy;
            return this;
        }

        public LoanBuilder status(String status) {
            this.status = status;
            return this;
        }

        public LoanBuilder reason(String reason) {
            this.reason = reason;
            return this;
        }

        public LoanBuilder aggregateId(UUID aggregateId) {
            this.aggregateId = aggregateId;
            return this;
        }

        public LoanBuilder version(int version) {
            this.version = version;
            return this;
        }

        public LoanBuilder timestamp(Instant timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public LoanBuilder eventType(String eventType) {
            this.eventType = eventType;
            return this;
        }

        public LoanBuilder aggregateType(String value) {
            this.aggregateType = value;
            return this;
        }

        public LoanResolutionApprovedEvent build() {
            return new LoanResolutionApprovedEvent(this);
        }
    }
}
