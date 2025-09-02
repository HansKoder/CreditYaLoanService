package org.pragma.creditya.model.loantype;
import lombok.Getter;
import org.pragma.creditya.model.loantype.valueobject.Description;
import org.pragma.creditya.model.loantype.valueobject.InterestRate;
import org.pragma.creditya.model.loantype.valueobject.LoanTypeId;
import org.pragma.creditya.model.shared.domain.model.entity.AggregateRoot;

@Getter
public class LoanType extends AggregateRoot<LoanTypeId> {

    private final Description description;
    private final InterestRate interestRate;

    private LoanType(Description description, InterestRate interestRate) {
        this.description = description;
        this.interestRate = interestRate;
    }

    public static final class LoanTypeBuilder {
        private String description;
        private Double interestRate;
        private Long id;

        private LoanTypeBuilder() {
        }

        public static LoanTypeBuilder aLoanType() {
            return new LoanTypeBuilder();
        }

        public LoanTypeBuilder description(String description) {
            this.description = description;
            return this;
        }

        public LoanTypeBuilder interestRate(Double interestRate) {
            this.interestRate = interestRate;
            return this;
        }

        public LoanTypeBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public LoanType build() {
            LoanType loanType = new LoanType(new Description(description), new InterestRate(interestRate));
            loanType.setId(new LoanTypeId(id));
            return loanType;
        }
    }
}
