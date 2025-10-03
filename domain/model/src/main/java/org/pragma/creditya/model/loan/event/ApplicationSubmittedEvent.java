package org.pragma.creditya.model.loan.event;

import lombok.*;
import org.pragma.creditya.model.loan.valueobject.LoanStatus;
import org.pragma.creditya.model.loantype.valueobject.ResolutionType;

import java.math.BigDecimal;

@ToString
@Builder
@Getter
@AllArgsConstructor
public class ApplicationSubmittedEvent implements LoanEventPayload{

    private String document;
    private BigDecimal amount;
    private BigDecimal monthlyDebt;

    private Long typeLoan;
    private LoanStatus status;
    private ResolutionType resolutionType;
    private int period;

    // In order to deserialize, should have a public empty constructor (this is mandatory)
    public ApplicationSubmittedEvent() {
    }
}
