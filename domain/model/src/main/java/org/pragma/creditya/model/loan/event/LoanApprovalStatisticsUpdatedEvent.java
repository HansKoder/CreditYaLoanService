package org.pragma.creditya.model.loan.event;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class LoanApprovalStatisticsUpdatedEvent extends LoanEvent {
    private BigDecimal amount;

    public LoanApprovalStatisticsUpdatedEvent() {
    }

    public LoanApprovalStatisticsUpdatedEvent(LoanBuilder loanBuilder) {
        super.setAggregateId(loanBuilder.aggregateId);
        super.setEventType(loanBuilder.eventType);
        super.setVersion(1);
        super.setAggregateType(loanBuilder.aggregateType);
        super.setDestination(EventDestination.SQS);

        this.amount = loanBuilder.amount;
    }

    public static final class LoanBuilder {
        private UUID aggregateId;
        private String eventType;
        private String aggregateType;

        private BigDecimal amount;

        private LoanBuilder() {
        }

        public static LoanBuilder aLoanApprovalBuilder() {
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

        public LoanBuilder aggregateType(BigDecimal value) {
            this.amount = value;
            return this;
        }

        public LoanApprovalStatisticsUpdatedEvent build() {
            return new LoanApprovalStatisticsUpdatedEvent(this);
        }
    }
}
