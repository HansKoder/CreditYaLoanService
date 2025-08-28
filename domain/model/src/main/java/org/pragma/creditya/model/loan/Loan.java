package org.pragma.creditya.model.loan;
import lombok.Getter;
import org.pragma.creditya.model.loan.valueobject.*;
import org.pragma.creditya.model.shared.domain.model.entity.AggregateRoot;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
public class Loan extends AggregateRoot<LoanId> {
    private final Document document;
    private final Amount amount;
    private final Period period;

    private final LoanStatus loanStatus;

    private Loan(LoanId id, Document document, Amount amount, Period period, LoanStatus loanStatus) {
        this.loanStatus = loanStatus;
        this.setId(id);
        this.document = document;
        this.amount = amount;
        this.period = period;
    }

    public static Loan createRequestLoan(String document, BigDecimal amount, LocalDate period) {
        return new Loan(
                new LoanId(UUID.randomUUID()),
                new Document(document),
                new Amount(amount),
                new Period(period),
                LoanStatus.PENDING
        );
    }

    public static Loan rebuild(UUID uuid, String document, BigDecimal amount, LocalDate period, LoanStatus status) {
        return new Loan(
                new LoanId(uuid),
                new Document(document),
                new Amount(amount),
                new Period(period),
                status
        );
    }
}
