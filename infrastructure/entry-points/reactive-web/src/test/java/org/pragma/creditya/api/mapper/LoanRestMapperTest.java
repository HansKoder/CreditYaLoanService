package org.pragma.creditya.api.mapper;

import org.junit.jupiter.api.Test;
import org.pragma.creditya.api.dto.request.CreateApplicationLoanRequest;
import org.pragma.creditya.api.dto.response.LoanApplicationResponse;
import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loan.valueobject.LoanStatus;
import org.pragma.creditya.usecase.command.CreateRequestLoanCommand;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoanRestMapperTest {

    private final UUID LOAN_ID_EXAMPLE = UUID.fromString("5b87a0d6-2fed-4db7-aa49-49663f719659");

    private final Loan LOAN_EXAMPLE = Loan.LoanBuilder.aLoan()
            .id(LOAN_ID_EXAMPLE)
            .loanTypeCode(1L)
            .loanStatus(LoanStatus.PENDING)
            .document("103")
            .period(1,0)
            .amount(BigDecimal.ONE)
            .build();

    private final CreateApplicationLoanRequest REQUEST = new CreateApplicationLoanRequest(
            "103",
            BigDecimal.ONE,
            1L,
            1,
            0
    );

    @Test
    void shouldBeMappedToCommand () {
        CreateRequestLoanCommand command = LoanRestMapper.toCommand(REQUEST);

        assertEquals("103", command.document());
        assertEquals(BigDecimal.ONE, command.amount());
        assertEquals(1L, command.loanTypeId());
        assertEquals(1, command.year());
        assertEquals(0, command.month());
    }

    @Test
    void shouldBeMappedToResponse () {
        LoanApplicationResponse response = LoanRestMapper.toResponse(LOAN_EXAMPLE);

        assertEquals("103", response.document());
        assertEquals(BigDecimal.ONE, response.amount());
        assertEquals(1L, response.loanTypeId());
        assertEquals(1, response.year());
        assertEquals(0, response.month());
        assertEquals("PENDING", response.status());
    }

}
