package org.pragma.creditya.model.loantype;
import lombok.Getter;
import org.pragma.creditya.model.loantype.valueobject.Description;
import org.pragma.creditya.model.loantype.valueobject.InterestRate;
import org.pragma.creditya.model.loantype.valueobject.LoanTypeId;
import org.pragma.creditya.model.shared.domain.model.entity.AggregateRoot;
import org.pragma.creditya.model.shared.domain.model.entity.BaseEntity;

import java.util.UUID;

@Getter
public class LoanType extends BaseEntity<LoanTypeId> {

    private final Description description;
    private final InterestRate interestRate;

    public LoanType(Description description, InterestRate interestRate) {
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

        public LoanTypeBuilder description(String value) {
            this.description = value;
            return this;
        }

        public LoanTypeBuilder interestRate(Double value) {
            this.interestRate = value;
            return this;
        }

        public LoanTypeBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public LoanType build() {
            LoanType entity = new LoanType(
                    new Description(this.description),
                    new InterestRate(this.interestRate)
            );

            entity.setId(new LoanTypeId(id));

            return entity;
        }
    }
}
