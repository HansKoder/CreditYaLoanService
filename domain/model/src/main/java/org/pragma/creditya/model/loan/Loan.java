package org.pragma.creditya.model.loan;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.pragma.creditya.model.loan.event.LoanApplicationSubmitted;
import org.pragma.creditya.model.loan.event.LoanEvent;
import org.pragma.creditya.model.loan.exception.LoanDomainException;
import org.pragma.creditya.model.loan.valueobject.*;
import org.pragma.creditya.model.shared.domain.model.entity.AggregateRoot;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@ToString
@Getter
public class Loan extends AggregateRoot<LoanId> {
    private Document document;
    private Amount amount;
    private Period period;
    private LoanType loanType;
    private LoanStatus loanStatus;

    // list of events generated
    private final List<LoanEvent> uncommittedEvents = new ArrayList<>();

    private final String LOAN_EVEN_TYPE =  "LOAN";

    private Loan(LoanBuilder builder) {
        this.document = builder.document;
        this.amount = builder.amount;
        this.period = builder.period;
        this.loanType = builder.loanType;
        this.loanStatus = builder.loanStatus;
        this.setId(builder.id);
    }

    public void checkApplicationLoan() {
        if ( this.getId() != null &&  this.getId().getValue() != null )
            throw new LoanDomainException("Must be without ID");

        if ( this.loanStatus != null && !this.loanStatus.equals(LoanStatus.PENDING) )
            throw new LoanDomainException("Invalid status to create request");

        this.loanStatus = LoanStatus.PENDING;
        this.setId(new LoanId(UUID.randomUUID()));

        LoanApplicationSubmitted event = LoanApplicationSubmitted.LoanBuilder.
                aLoanApplicationSubmitted()
                .aggregateId(getId().getValue())
                .aggregateType(LOAN_EVEN_TYPE)
                .eventType(LoanApplicationSubmitted.class.getSimpleName())
                .timestamp(Instant.now())
                .document(this.document.value())
                .status(loanStatus.name())
                .amount(this.amount.amount())
                .typeLoan(this.loanType.code())
                .build();

        this.uncommittedEvents.add(event);
    }

    public void checkResolution () {
        // check status must be pending.

        // check type resolution (REJECTED - APPROVED) in case, does not continue.

        // if rejected must be mandatory reason.

        // all rules are ok change status must be RESOLUTION (APPROVED, REJECTED)
        // this section should return a LoanType -> ? like LonRejected ? LoanApproved ? instead void.
    }

    public List<LoanEvent> getUncommittedEvents() {
        return Collections.unmodifiableList(uncommittedEvents);
    }

    public void clearUncommittedEvents() {
        this.uncommittedEvents.clear();
    }

    public static final class LoanBuilder {
        private Amount amount;
        private Document document;
        private Period period;
        private LoanType loanType;
        private LoanStatus loanStatus;
        private LoanId id;

        private LoanBuilder() {
        }

        public static LoanBuilder aLoan() {
            return new LoanBuilder();
        }

        public LoanBuilder amount(BigDecimal amount) {
            this.amount = new Amount(amount);
            return this;
        }

        public LoanBuilder document(String document) {
            this.document = new Document(document);
            return this;
        }

        public LoanBuilder period(int year, int month) {
            this.period = new Period(year, month);
            return this;
        }

        public LoanBuilder loanType(Long loanType) {
            this.loanType = new LoanType(loanType);
            return this;
        }

        public LoanBuilder loanStatus(LoanStatus loanStatus) {
            this.loanStatus = loanStatus;
            return this;
        }

        public LoanBuilder id(UUID id) {
            this.id = new LoanId(id);
            return this;
        }

        public Loan build() {
            return new Loan(this);
        }
    }
}
