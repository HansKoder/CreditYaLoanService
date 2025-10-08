package org.pragma.creditya.usecase.outbox.payload;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@ToString
@Getter
@Setter
@Builder
public class ReportOutboxPayload implements OutboxPayload {

    private String loanId;
    private BigDecimal amount;

}
