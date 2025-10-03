package org.pragma.creditya.model.loan;
import lombok.Getter;
import lombok.ToString;
import org.pragma.creditya.model.customer.valueobject.Document;
import org.pragma.creditya.model.loan.event.*;
import org.pragma.creditya.model.loan.exception.AmountLoanIsNotEnoughDomainException;
import org.pragma.creditya.model.loan.exception.LoanDomainException;
import org.pragma.creditya.model.loan.factory.LoanEventFactory;
import org.pragma.creditya.model.loan.valueobject.*;
import org.pragma.creditya.model.loantype.valueobject.LoanTypeId;
import org.pragma.creditya.model.loantype.valueobject.ResolutionType;
import org.pragma.creditya.model.shared.domain.model.entity.AggregateRoot;
import org.pragma.creditya.model.shared.domain.model.valueobject.Amount;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@ToString
@Getter
public class Loan extends AggregateRoot<LoanId> {

    private Document document;
    private LoanTypeId loanTypeId;

    private Amount amount;
    private Period period;
    private LoanStatus loanStatus;

    // must be relocated in a Value Object (maybe)
    private Resolution resolution;

    // expand to possibility to support contractOffered, contractSigned, etc.

    private Amount monthlyDebt;

    private final List<LoanEvent> uncommittedEvents = new ArrayList<>();

    private Loan(LoanBuilder builder) {
        this.setId(builder.id);
        this.amount = builder.amount;
        this.period = builder.period;
        this.loanStatus = builder.loanStatus;
        this.document = builder.document;
        this.loanTypeId = builder.loanTypeId;
    }

    // Business Rules
    public void checkApplicationLoan() {
        if ( this.getId() != null &&  this.getId().getValue() != null )
            throw new LoanDomainException("Must be without ID");

        if ( this.loanStatus != null && !this.loanStatus.equals(LoanStatus.PENDING) )
            throw new LoanDomainException("Invalid status to create request");

        calculateTotalMonthlyDebt();
    }

    public void markAsSubmitted (ResolutionType resolutionType) {
        this.loanStatus = LoanStatus.PENDING;
        this.setId(new LoanId(UUID.randomUUID()));
        uncommittedEvents.add(LoanEventFactory.applicationSubmittedLoan(this, resolutionType));
    }

    public void resolutionApplicationLoan (Resolution resolution) {
        this.resolution = resolution;
         checkBeforeBeingResolved();

        this.loanStatus = resolution.decision();
        this.uncommittedEvents.add(getLoanResolvedEvent());
    }

    private LoanEvent getLoanResolvedEvent() {
        return switch (resolution.decision()) {
            case LoanStatus.APPROVED -> LoanEventFactory.applicationApprovedLoan(this);
            case LoanStatus.REJECTED -> LoanEventFactory.applicationRejectedLoan(this);
            default -> throw new LoanDomainException("Unexcepted Decision, should be checked");
        };
    }

    private void checkBeforeBeingResolved () {
        checkIdBeforeBeingResolved();
        checkStatusBeforeBeingResolved();
        checkDecidedBy();
    }

    private void calculateTotalMonthlyDebt () {
        int totalMonths = period.calculateTotalMonths();

        if (!amount.isGreaterThan(new BigDecimal(totalMonths)))
            throw new AmountLoanIsNotEnoughDomainException("Amount is not enough, Total months is greater than amount loan");

        BigDecimal debt = amount.amount()
                .divide(BigDecimal.valueOf(totalMonths), 2, RoundingMode.HALF_UP);

        monthlyDebt = new Amount(debt);
    }

    private void checkDecidedBy () {
        if (resolution.by() == null || resolution.by().isBlank()) {
            String err = "In order to resolve the loan application, it must be responsible for giving the resolution.";
            throw new LoanDomainException(err);
        }
    }

    private void checkStatusBeforeBeingResolved () {
        if (this.loanStatus != LoanStatus.PENDING)
            throw new LoanDomainException("In order to resolve loan application, it must be with status PENDING");
    }

    private void checkIdBeforeBeingResolved () {
        if (this.getId() == null || this.getId().getValue() == null)
            throw new LoanDomainException("In order to resolve Loan Application, it must be persisted Loan, Loan without ID cannot continue with the resolution process");
    }

    public static Loan rehydrate(List<LoanEvent> history) {
        if (history == null || history.isEmpty())
            throw new LoanDomainException("Cannot rehydrate Loan without events");

        Loan loan = new LoanBuilder().build();

        for (LoanEvent event : history)
            loan.apply(event);

        loan.clearUncommittedEvents();
        return loan;
    }

    private void apply(LoanEvent event) {
        switch (event.getPayload()) {
            case ApplicationSubmittedEvent e -> applyLoanApplicationSubmitted(event);
            case ApplicationApprovedEvent e -> applyLoanApproved(e);
            case ApplicationRejectedEvent e -> applyLoanRejected(e);
            default -> throw new LoanDomainException("Unknown event type: " + event.getClass().getName());
        }
    }

    private void applyLoanApplicationSubmitted(LoanEvent event) {
        this.setId(new LoanId(event.getAggregateId()));

        ApplicationSubmittedEvent payload = (ApplicationSubmittedEvent) event.getPayload();

        this.loanStatus = payload.getStatus();
        this.amount = new Amount(payload.getAmount());
        this.period = new Period(1, 2); // Pending for be adjusted
        this.monthlyDebt = new Amount(payload.getMonthlyDebt());

        this.document = new Document(payload.getDocument());
        this.loanTypeId = new LoanTypeId(payload.getTypeLoan());
    }

    private void applyLoanApproved(ApplicationApprovedEvent e) {
        this.loanStatus = LoanStatus.APPROVED;
        this.resolution = new Resolution(e.getApprovedBy(), e.getReason(), e.getStatus());
    }

    private void applyLoanRejected(ApplicationRejectedEvent e) {
        this.loanStatus = LoanStatus.REJECTED;
        this.resolution = new Resolution(e.getRejectedBy(), e.getReason(), e.getStatus());
    }

    public List<LoanEvent> getUncommittedEvents() {
        return Collections.unmodifiableList(uncommittedEvents);
    }

    public void clearUncommittedEvents() {
        this.uncommittedEvents.clear();
    }

    // Builder custom
    public static final class LoanBuilder {

        private LoanId id;
        private Amount amount;
        private Period period;
        private LoanStatus loanStatus;

        private Document document;
        private LoanTypeId loanTypeId;

        private LoanBuilder() {
        }

        public static LoanBuilder aLoan() {
            return new LoanBuilder();
        }

        public LoanBuilder amount(BigDecimal amount) {
            this.amount = new Amount(amount);
            return this;
        }

        public LoanBuilder period(int year, int month) {
            this.period = new Period(year, month);
            return this;
        }

        public LoanBuilder loanStatus(LoanStatus loanStatus) {
            this.loanStatus = loanStatus;
            return this;
        }

        public LoanBuilder document(String value) {
            this.document = new Document(value);
            return this;
        }

        public LoanBuilder loanTypeId(Long value) {
            this.loanTypeId = new LoanTypeId(value);
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
