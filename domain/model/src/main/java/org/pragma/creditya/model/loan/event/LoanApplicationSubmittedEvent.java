package org.pragma.creditya.model.loan.event;

import lombok.Getter;
import lombok.Setter;
import org.pragma.creditya.model.loan.valueobject.LoanStatus;
import org.pragma.creditya.model.loantype.valueobject.ResolutionType;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class LoanApplicationSubmittedEvent extends LoanEvent{
    private String document;
    private BigDecimal amount;
    private BigDecimal monthlyDebt;

    private Long typeLoan;
    private LoanStatus status;
    private ResolutionType resolutionType;
    private int period;

    public LoanApplicationSubmittedEvent() {
    }

    private LoanApplicationSubmittedEvent(SubmittedBuilder builder) {
        super.setId(builder.eventId);
        super.setAggregateId(builder.aggregateId);
        super.setEventType(builder.eventType);
        super.setAggregateType(builder.aggregateType);

        this.document = builder.document;
        this.amount = builder.amount;
        this.typeLoan = builder.typeLoan;
        this.status = builder.status;
        this.period = builder.period;
        this.monthlyDebt = builder.monthlyDebt;
        this.resolutionType = builder.resolutionType;
    }

    public static final class SubmittedBuilder {

        // Loan Event
        private UUID eventId;
        private UUID aggregateId;
        private EventType eventType;
        private AggregateType aggregateType;
        private LoanStatus status;

        // Submitted Event
        private String document;
        private BigDecimal amount;
        private BigDecimal monthlyDebt;
        private int period;
        private Long typeLoan;

        private ResolutionType resolutionType;

        public static SubmittedBuilder aSubmittedEvent() {
            return new SubmittedBuilder();
        }

        public SubmittedBuilder eventId(UUID value) {
            this.eventId = value;
            return this;
        }

        public SubmittedBuilder aggregateId(UUID aggregateId) {
            this.aggregateId = aggregateId;
            return this;
        }

        public SubmittedBuilder eventType(EventType eventType) {
            this.eventType = eventType;
            return this;
        }

        public SubmittedBuilder aggregateType(AggregateType value) {
            this.aggregateType = value;
            return this;
        }

        public SubmittedBuilder document(String document) {
            this.document = document;
            return this;
        }

        public SubmittedBuilder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public SubmittedBuilder typeLoan(Long typeLoan) {
            this.typeLoan = typeLoan;
            return this;
        }

        public SubmittedBuilder status(LoanStatus status) {
            this.status = status;
            return this;
        }

        public SubmittedBuilder period(int value) {
            this.period = value;
            return this;
        }

        public SubmittedBuilder monthlyDebt(BigDecimal value) {
            this.monthlyDebt = value;
            return this;
        }

        public SubmittedBuilder resolutionType(ResolutionType value) {
            this.resolutionType = value;
            return this;
        }

        public LoanApplicationSubmittedEvent build() {
            return new LoanApplicationSubmittedEvent(this);
        }
    }
}
