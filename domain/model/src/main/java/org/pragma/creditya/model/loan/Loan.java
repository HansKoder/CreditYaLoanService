package org.pragma.creditya.model.loan;
import lombok.Getter;
import lombok.ToString;
import org.pragma.creditya.model.loan.entity.CustomerRead;
import org.pragma.creditya.model.loan.event.LoanApplicationSubmittedEvent;
import org.pragma.creditya.model.loan.event.LoanEvent;
import org.pragma.creditya.model.loan.event.LoanResolutionApprovedEvent;
import org.pragma.creditya.model.loan.event.LoanResolutionRejectedEvent;
import org.pragma.creditya.model.loan.exception.AmountLoanIsNotEnoughDomainException;
import org.pragma.creditya.model.loan.exception.LoanDomainException;
import org.pragma.creditya.model.loan.valueobject.*;
import org.pragma.creditya.model.loantype.LoanType;
import org.pragma.creditya.model.shared.domain.model.entity.AggregateRoot;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@ToString
@Getter
public class Loan extends AggregateRoot<LoanId> {
    private final Document document;
    private final Amount amount;
    private final Period period;
    private final LoanTypeCode loanTypeCode;
    private LoanStatus loanStatus;

    // those snapshot must be removed since, projections should add webclient request using document for getting all customer data (CQRS)
    // snapshots - for building rich events - projections
    private CustomerRead customer;
    private LoanType loanType;

    private String responsible;

    // calculate data
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

        createEventApplicationLoan();
    }

    // Load read customer and loanType (snapshots) // next iteration must be removed
    public void loadCustomer (CustomerRead customer) {
        this.customer = customer;
    }

    public void loadLoanType (LoanType loanType) {
        this.loanType = loanType;
    }

    // private methods business rules
    private void calculateTotalMonthlyDebt () {
        int totalMonths = period.calculateTotalMonths();

        if (!amount.isGreaterThan(new BigDecimal(totalMonths)))
            throw new AmountLoanIsNotEnoughDomainException("Amount is not enough, Total months is greater than amount loan");

        BigDecimal debt = amount.amount()
                .divide(BigDecimal.valueOf(totalMonths), 2, RoundingMode.HALF_UP);

        totalMonthlyDebt = new Amount(debt);
    }

    private void createEventApplicationLoan () {
        LoanApplicationSubmittedEvent event = LoanApplicationSubmittedEvent.LoanBuilder.
                aLoanApplicationSubmitted()
                .aggregateId(getId().getValue())
                .aggregateType(AGGREGATE_TYPE)
                .eventType(LoanApplicationSubmittedEvent.class.getSimpleName())
                .timestamp(Instant.now())
                .document(this.document.value())
                .status(loanStatus.name())
                .amount(this.amount.amount())
                .typeLoan(this.loanTypeCode.code())
                .period(this.period.calculateTotalMonths())
                .baseSalary(this.customer.getBaseSalary())
                .email(this.customer.getEmail())
                .name(this.customer.getName())
                .interestRate(this.loanType.getInterestRate().value())
                .typeLoanDescription(this.loanType.getDescription().value())
                .totalMonthlyDebt(this.totalMonthlyDebt.amount())
                .build();

        this.uncommittedEvents.add(event);
    }

    // Events publisher
    public List<LoanEvent> getUncommittedEvents() {
        return Collections.unmodifiableList(uncommittedEvents);
    }

    public void clearUncommittedEvents() {
        this.uncommittedEvents.clear();
    }

    private void checkBeforeBeingResolved (String resolution) {
        checkIdBeforeBeingResolved(resolution);
        checkStatusBeforeBeingResolved(resolution);
        checkResponsible(resolution);
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

    public void loadAuthorResolutionLoan (String username) {
        this.responsible = username;
    }

    public void checkApprovedLoan(String reason) {
        checkBeforeBeingResolved(APPROVED);

        this.loanStatus = LoanStatus.APPROVED;

        LoanResolutionApprovedEvent event = LoanResolutionApprovedEvent.LoanBuilder.aLoanResolutionApproved()
                .approvedBy(responsible)
                .aggregateId(getId().getValue())
                .reason("")
                .build();

        uncommittedEvents.add(event);
    }

    public void checkRejectedLoan(String reason) {
        checkBeforeBeingResolved(REJECTED);

        // Condition reason for being rejected

        this.loanStatus = LoanStatus.REJECTED;

        LoanResolutionRejectedEvent event = LoanResolutionRejectedEvent.LoanBuilder.aLoanResolutionRejected()
                .rejectedBy("")
                .aggregateId(getId().getValue())
                .reason(reason)
                .build();

        uncommittedEvents.add(event);
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
