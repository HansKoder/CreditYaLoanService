package org.pragma.creditya.model.loan;
import lombok.Getter;
import org.pragma.creditya.model.loan.exception.LoanDomainException;
import org.pragma.creditya.model.loan.valueobject.*;
import org.pragma.creditya.model.shared.domain.model.entity.AggregateRoot;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class Loan extends AggregateRoot<LoanId> {
    private final Document document;
    private final Amount amount;
    private final Period period;
    private final LoanType loanType;
    private LoanStatus loanStatus;

    private Loan(LoanId id, Document document, Amount amount, Period period, LoanType loanType, LoanStatus loanStatus) {
        this.loanStatus = loanStatus;
        this.setId(id);
        this.document = document;
        this.amount = amount;
        this.loanType = loanType;
        this.period = period;
    }

    public void createRequestLoan() {
        if ( this.getId().getValue() != null )
            throw new LoanDomainException("Must be without ID");

        if ( this.loanStatus != null && !this.loanStatus.equals(LoanStatus.PENDING) )
            throw new LoanDomainException("Invalid status to create request");

        this.loanStatus = LoanStatus.PENDING;
    }

    public static final class LoanBuilder {
        private BigDecimal amount;
        private String document;
        private int year;
        private int month;
        private Long loanType;
        private LoanStatus loanStatus;
        private UUID id;

        private LoanBuilder() {
        }

        public static LoanBuilder aLoan() {
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

        public LoanBuilder period(int year, int month) {
            this.year = year;
            this.month = month;
            return this;
        }

        public LoanBuilder loanType(Long loanType) {
            this.loanType = loanType;
            return this;
        }

        public LoanBuilder loanStatus(LoanStatus loanStatus) {
            this.loanStatus = loanStatus;
            return this;
        }

        public LoanBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public Loan build() {
            return new Loan(
                    new LoanId(id),
                    new Document(document),
                    new Amount(amount),
                    new Period(year, month),
                    new LoanType(loanType),
                    loanStatus);
        }
    }
}
