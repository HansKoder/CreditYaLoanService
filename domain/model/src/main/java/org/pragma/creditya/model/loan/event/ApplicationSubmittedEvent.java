package org.pragma.creditya.model.loan.event;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.pragma.creditya.model.loan.valueobject.LoanStatus;
import org.pragma.creditya.model.loantype.valueobject.ResolutionType;

import java.math.BigDecimal;

@Builder
@Getter
public class ApplicationSubmittedEvent implements LoanEventPayload{

    private final String document;
    private final BigDecimal amount;
    private final BigDecimal monthlyDebt;

    private final Long typeLoan;
    private final LoanStatus status;
    private final ResolutionType resolutionType;
    private final int period;

}
