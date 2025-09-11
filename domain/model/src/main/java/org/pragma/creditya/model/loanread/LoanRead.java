package org.pragma.creditya.model.loanread;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class LoanRead {
    private String id;
    private UUID loanId;
    private String document;
    private BigDecimal amount;

    @Override
    public String toString() {
        return "LoanRead{" +
                "amount=" + amount +
                ", loanId=" + loanId +
                ", document='" + document + '\'' +
                '}';
    }
}
