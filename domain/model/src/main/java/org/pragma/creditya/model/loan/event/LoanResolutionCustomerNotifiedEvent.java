package org.pragma.creditya.model.loan.event;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class LoanResolutionCustomerNotifiedEvent extends LoanEvent{
    private String document;
    private String decision;
    private String reason;

    public LoanResolutionCustomerNotifiedEvent() {
    }

    public LoanResolutionCustomerNotifiedEvent(LoanBuilder loanBuilder) {
        super.setAggregateId(loanBuilder.aggregateId);
        super.setEventType(loanBuilder.eventType);
        super.setVersion(1);
        super.setAggregateType(loanBuilder.aggregateType);
        super.setDestination(EventDestination.SQS);

        this.document = loanBuilder.document;
        this.reason = loanBuilder.reason;
        this.decision = loanBuilder.decision;
    }

    public static final class LoanBuilder {
        private UUID aggregateId;
        private String eventType;
        private String aggregateType;

        private String document;
        private String decision;
        private String reason;

        private LoanBuilder() {
        }

        public static LoanBuilder aLoanResolutionApproved() {
            return new LoanBuilder();
        }

        public LoanBuilder aggregateId(UUID aggregateId) {
            this.aggregateId = aggregateId;
            return this;
        }

        public LoanBuilder eventType(String value) {
            this.eventType = value;
            return this;
        }

        public LoanBuilder aggregateType(String value) {
            this.aggregateType = value;
            return this;
        }

        public LoanBuilder document(String value) {
            this.document = value;
            return this;
        }

        public LoanBuilder decision(String value) {
            this.decision = value;
            return this;
        }

        public LoanBuilder reason(String value) {
            this.reason = value;
            return this;
        }

        public LoanResolutionCustomerNotifiedEvent build() {
            return new LoanResolutionCustomerNotifiedEvent(this);
        }
    }
}
