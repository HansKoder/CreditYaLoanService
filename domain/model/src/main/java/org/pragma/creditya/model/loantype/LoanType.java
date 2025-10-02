package org.pragma.creditya.model.loantype;
import lombok.Getter;
import lombok.Setter;
import org.pragma.creditya.model.loantype.valueobject.Description;
import org.pragma.creditya.model.loantype.valueobject.InterestRate;
import org.pragma.creditya.model.loantype.valueobject.LoanTypeId;
import org.pragma.creditya.model.loantype.valueobject.ResolutionType;
import org.pragma.creditya.model.shared.domain.model.entity.BaseEntity;

@Getter
@Setter
public class LoanType extends BaseEntity<LoanTypeId> {

    private Description description;
    private InterestRate interestRate;
    private ResolutionType resolutionType;

    public LoanType(LoanTypeBuilder builder) {
        this.setId(builder.id);
        this.description = builder.description;
        this.interestRate = builder.interestRate;
        this.resolutionType = builder.resolutionType;
    }

    // Builder
    public static final class LoanTypeBuilder {
        private Description description;
        private InterestRate interestRate;
        private LoanTypeId id;
        private ResolutionType resolutionType;

        private LoanTypeBuilder() {
        }

        public static LoanTypeBuilder aLoanType() {
            return new LoanTypeBuilder();
        }

        public LoanTypeBuilder description(String value) {
            this.description = new Description(value);
            return this;
        }

        public LoanTypeBuilder interestRate(Double value) {
            this.interestRate = new InterestRate(value);
            return this;
        }

        public LoanTypeBuilder id(Long id) {
            this.id = new LoanTypeId(id);
            return this;
        }

        public LoanTypeBuilder selfDecision(Boolean value) {
            this.resolutionType = new ResolutionType(value);
            return this;
        }

        public LoanType build() {
            return new LoanType(this);
        }
    }
}
