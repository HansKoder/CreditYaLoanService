package org.pragma.creditya.model.loan.event;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
public class LoanApplicationSubmittedEvent extends LoanEvent{
    private final String document;
    private final String name;
    private final String email;
    private final BigDecimal baseSalary;
    private final BigDecimal amount;
    private final BigDecimal totalMonthlyDebt;

    private final Long typeLoan;
    private final String typeLoanDescription;
    private final Double interestRate;

    private final String status;
    private final int period;

    private LoanApplicationSubmittedEvent(LoanBuilder loanBuilder) {
        super(loanBuilder.aggregateId, loanBuilder.version, loanBuilder.timestamp, loanBuilder.eventType, loanBuilder.aggregateType);

        this.document = loanBuilder.document;
        this.amount = loanBuilder.amount;
        this.typeLoan = loanBuilder.typeLoan;
        this.status = loanBuilder.status;
        this.period = loanBuilder.period;
        this.name = loanBuilder.name;
        this.email = loanBuilder.email;
        this.baseSalary = loanBuilder.baseSalary;
        this.typeLoanDescription = loanBuilder.typeLoanDescription;
        this.interestRate = loanBuilder.interestRate;
        this.totalMonthlyDebt = loanBuilder.totalMonthlyDebt;
    }

    public static final class LoanBuilder {
        private BigDecimal totalMonthlyDebt;
        private String name;
        private String email;
        private BigDecimal baseSalary;

        private String typeLoanDescription;
        private Double interestRate;

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

        public LoanBuilder name(String value) {
            this.name = value;
            return this;
        }

        public LoanBuilder typeLoanDescription(String value) {
            this.typeLoanDescription = value;
            return this;
        }

        public LoanBuilder baseSalary(BigDecimal value) {
            this.baseSalary = value;
            return this;
        }


        public LoanBuilder interestRate(Double value) {
            this.interestRate = value;
            return this;
        }

        public LoanBuilder email(String value) {
            this.email = value;
            return this;
        }

        public LoanBuilder totalMonthlyDebt(BigDecimal value) {
            this.totalMonthlyDebt = value;
            return this;
        }

        public LoanApplicationSubmittedEvent build() {
            return new LoanApplicationSubmittedEvent(this);
        }
    }
}
