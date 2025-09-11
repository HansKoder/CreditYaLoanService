package org.pragma.creditya.model.loan.event;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
public class LoanApplicationSubmittedEvent extends LoanEvent{
    private final String document;
    private final BigDecimal amount;
    private final Long typeLoan;
    private final String status;
    private final int period;

    private LoanApplicationSubmittedEvent(LoanBuilder loanBuilder) {
        super(loanBuilder.aggregateId, loanBuilder.version, loanBuilder.timestamp, loanBuilder.eventType, loanBuilder.aggregateType);

        this.document = loanBuilder.document;
        this.amount = loanBuilder.amount;
        this.typeLoan = loanBuilder.typeLoan;
        this.status = loanBuilder.status;
        this.period = loanBuilder.period;
    }

    public static final class LoanBuilder {
        private BigDecimal amount;
        private String document;
        private Long typeLoan;
        private String status;
        private UUID aggregateId;
        private int version;
        private Instant timestamp;
        private String eventType;
        private String aggregateType;
        private int period;

        public static LoanBuilder aLoanApplicationSubmitted() {
            return new LoanBuilder();
        }

        public LoanBuilder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public LoanBuilder document(String document) {
            this.document = document;
            return this;
        }

        public LoanBuilder typeLoan(Long typeLoan) {
            this.typeLoan = typeLoan;
            return this;
        }

        public LoanBuilder status(String status) {
            this.status = status;
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

        public LoanBuilder period(int value) {
            this.period = value;
            return this;
        }

        public LoanApplicationSubmittedEvent build() {
            return new LoanApplicationSubmittedEvent(this);
        }
    }
}
