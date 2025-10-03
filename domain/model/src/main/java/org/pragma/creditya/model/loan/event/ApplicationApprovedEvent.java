package org.pragma.creditya.model.loan.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.pragma.creditya.model.loan.valueobject.LoanStatus;

@ToString
@Getter
@Builder
@AllArgsConstructor
public class ApplicationApprovedEvent implements LoanEventPayload {

    private LoanStatus status;
    private String reason;
    private String approvedBy;

    // In order to deserialize, should have a public empty constructor (this is mandatory)
    public ApplicationApprovedEvent() {
    }
}
