package org.pragma.creditya.model.loan.event;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class LoanResolutionRejectedEvent extends LoanEvent{
    private final String status;
    private final String reason;
    private final String rejectedBy;


    public LoanResolutionRejectedEvent(LoanBuilder loanBuilder) {
        super(loanBuilder.aggregateId, loanBuilder.version, loanBuilder.timestamp, loanBuilder.eventType, loanBuilder.aggregateType);

        this.status = loanBuilder.status;
        this.reason = loanBuilder.reason;
        this.rejectedBy = loanBuilder.rejectedBy;
    }

    public static final class LoanBuilder {
        private String reason;
        private String status;
        private String rejectedBy;
        private UUID aggregateId;
        private int version;
        private Instant timestamp;
        private String eventType;
        private String aggregateType;

        private LoanBuilder() {
        }

        public static LoanBuilder aLoanResolutionRejected() {
            return new LoanBuilder();
        }

        public LoanBuilder reason(String reason) {
            this.reason = reason;
            return this;
        }

        public LoanBuilder status(String status) {
            this.status = status;
            return this;
        }

        public LoanBuilder rejectedBy(String rejectedBy) {
            this.rejectedBy = rejectedBy;
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


        public LoanResolutionRejectedEvent build() {
            return new LoanResolutionRejectedEvent(this);
        }
    }
}
