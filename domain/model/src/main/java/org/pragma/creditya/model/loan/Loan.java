package org.pragma.creditya.model.loan;
import lombok.Getter;
import lombok.ToString;
import org.pragma.creditya.model.loan.event.LoanApplicationSubmittedEvent;
import org.pragma.creditya.model.loan.event.LoanEvent;
import org.pragma.creditya.model.loan.event.LoanResolutionApprovedEvent;
import org.pragma.creditya.model.loan.event.LoanResolutionRejectedEvent;
import org.pragma.creditya.model.loan.exception.AmountLoanIsNotEnoughDomainException;
import org.pragma.creditya.model.loan.exception.LoanDomainException;
import org.pragma.creditya.model.loan.factory.LoanEventFactory;
import org.pragma.creditya.model.loan.valueobject.*;
import org.pragma.creditya.model.shared.domain.model.entity.AggregateRoot;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    private LoanTypeCode loanTypeCode;
    private LoanStatus loanStatus;

    private String responsible;
    private String reason;

    private Amount totalMonthlyDebt;

    private final List<LoanEvent> uncommittedEvents = new ArrayList<>();
    private final String  AGGREGATE_TYPE =  "LOAN";

    private final String APPROVED = "approved";
    private final String REJECTED = "rejected";

    private Loan(LoanBuilder builder) {
        this.document = builder.document;
        this.amount = builder.amount;
        this.period = builder.period;
        this.loanTypeCode = builder.loanTypeCode;
        this.loanStatus = builder.loanStatus;
        this.setId(builder.id);
    }

    // Business Rules
    public void checkApplicationLoan() {
        if ( this.getId() != null &&  this.getId().getValue() != null )
            throw new LoanDomainException("Must be without ID");

        if ( this.loanStatus != null && !this.loanStatus.equals(LoanStatus.PENDING) )
            throw new LoanDomainException("Invalid status to create request");

        calculateTotalMonthlyDebt();
    }

    public void markAsPending () {
        this.loanStatus = LoanStatus.PENDING;
        this.setId(new LoanId(UUID.randomUUID()));
        uncommittedEvents.add(LoanEventFactory.submittedEvent(this));
    }

    public void loadAuthorResolutionLoan (String username) {
        this.responsible = username;
    }

    public void checkApprovedLoan(String reason) {
        checkBeforeBeingResolved(APPROVED);

        this.loanStatus = LoanStatus.APPROVED;

        uncommittedEvents.add(LoanEventFactory.approvedEvent(this));
    }

    public void checkRejectedLoan(String reason) {
        checkBeforeBeingResolved(REJECTED);

        this.loanStatus = LoanStatus.REJECTED;

        uncommittedEvents.add(LoanEventFactory.rejectedEvent(this));
    }

    // private methods business rules
    private void checkBeforeBeingResolved (String resolution) {
        checkIdBeforeBeingResolved(resolution);
        checkStatusBeforeBeingResolved(resolution);
        checkResponsible(resolution);
    }

    private void calculateTotalMonthlyDebt () {
        int totalMonths = period.calculateTotalMonths();

        if (!amount.isGreaterThan(new BigDecimal(totalMonths)))
            throw new AmountLoanIsNotEnoughDomainException("Amount is not enough, Total months is greater than amount loan");

        BigDecimal debt = amount.amount()
                .divide(BigDecimal.valueOf(totalMonths), 2, RoundingMode.HALF_UP);

        totalMonthlyDebt = new Amount(debt);
    }

    private void checkResponsible (String resolution) {
        if (responsible == null || responsible.isBlank()) {
            String err = "Who is responsible for this loan, Must have a responsible for being " + resolution;
            throw new LoanDomainException(err);
        }
    }

    private void checkStatusBeforeBeingResolved (String  resolution) {
        if (this.loanStatus != LoanStatus.PENDING)
            throw new LoanDomainException("Must have status Pending for being " + resolution);
    }

    private void checkIdBeforeBeingResolved (String  resolution) {
        if (this.getId() == null || this.getId().getValue() == null)
            throw new LoanDomainException("Must have ID Loan for being " + resolution);
    }

    public static Loan rehydrate(List<LoanEvent> history) {
        if (history == null || history.isEmpty())
            throw new LoanDomainException("Cannot rehydrate Loan without events");

        Loan loan = new LoanBuilder().build();

        for (LoanEvent event : history)
            loan.apply(event);

        loan.clearUncommittedEvents(); // clear
        return loan;
    }

    private void apply(LoanEvent event) {
        switch (event) {
            case LoanApplicationSubmittedEvent e -> applyLoanApplicationSubmitted(e);
            case LoanResolutionApprovedEvent e -> applyLoanApproved(e);
            case LoanResolutionRejectedEvent e -> applyLoanRejected(e);
            default -> throw new LoanDomainException("Unknown event type: " + event.getClass().getName());
        }
    }

    private void applyLoanApplicationSubmitted(LoanApplicationSubmittedEvent e) {
        this.setId(new LoanId(e.getAggregateId()));
        this.loanStatus = LoanStatus.valueOf(e.getStatus());
        this.document = new Document(e.getDocument());
        this.amount = new Amount(e.getAmount());
        this.period = new Period(0, e.getPeriod());
        this.loanTypeCode = new LoanTypeCode(e.getTypeLoan());

        this.totalMonthlyDebt = new Amount(e.getTotalMonthlyDebt());
    }

    private void applyLoanApproved(LoanResolutionApprovedEvent e) {
        this.loanStatus = LoanStatus.APPROVED;
        this.responsible = e.getApprovedBy();
    }

    private void applyLoanRejected(LoanResolutionRejectedEvent e) {
        this.loanStatus = LoanStatus.REJECTED;
        this.responsible = e.getRejectedBy();
    }

    public List<LoanEvent> getUncommittedEvents() {
        return Collections.unmodifiableList(uncommittedEvents);
    }

    public void clearUncommittedEvents() {
        this.uncommittedEvents.clear();
    }

    // Builder custom
    public static final class LoanBuilder {
        private Amount amount;
        private Document document;
        private Period period;
        private LoanTypeCode loanTypeCode;
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

        public LoanBuilder loanTypeCode(Long loanType) {
            this.loanTypeCode = new LoanTypeCode(loanType);
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
