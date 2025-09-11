package org.pragma.creditya.model.loan;

import org.junit.jupiter.api.Test;
import org.pragma.creditya.model.loan.entity.CustomerRead;
import org.pragma.creditya.model.loan.exception.AmountLoanIsNotEnoughDomainException;
import org.pragma.creditya.model.loan.exception.LoanDomainException;
import org.pragma.creditya.model.loan.valueobject.LoanStatus;
import org.pragma.creditya.model.loantype.LoanType;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class LoadTest {

    @Test
    void shouldThrowExceptionWhenAmountIsNegative () {
        LoanDomainException exception = assertThrows(LoanDomainException.class, () -> {
            Loan.LoanBuilder
                    .aLoan()
                    .document("123")
                    .amount(BigDecimal.valueOf(-5))
                    .build();
        });

        assertEquals("Amount must be positive", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenYearIsNegative () {
        LoanDomainException exception = assertThrows(LoanDomainException.class, () -> {
            Loan.LoanBuilder
                    .aLoan()
                    .document("123")
                    .amount(BigDecimal.valueOf(10))
                    .period(-5, 6)
                    .build();
        });

        assertEquals("Year must be greater or equal to zero", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenLoanTypeIsNull () {
        LoanDomainException exception = assertThrows(LoanDomainException.class, () -> {
            Loan.LoanBuilder
                    .aLoan()
                    .document("123")
                    .amount(BigDecimal.valueOf(10))
                    .period(1, 6)
                    .loanTypeCode(null)
                    .build();
        });

        assertEquals("Loan Type must be mandatory", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenUUIDIsDefined () {
        Loan domain = Loan.LoanBuilder
                .aLoan()
                .id(UUID.fromString("e36702e6-78d0-4368-a191-292f53c8141c"))
                .document("123")
                .amount(BigDecimal.valueOf(10))
                .period(1, 6)
                .loanTypeCode(1L)
                .build();

        LoanDomainException exception = assertThrows(LoanDomainException.class, domain::checkApplicationLoan);

        assertEquals("Must be without ID", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenStatusIsInvalid () {
        Loan domain = Loan.LoanBuilder
                .aLoan()
                .document("123")
                .amount(BigDecimal.valueOf(10))
                .period(1, 6)
                .loanTypeCode(1L)
                .loanStatus(LoanStatus.APPROVED)
                .build();

        LoanDomainException exception = assertThrows(LoanDomainException.class, domain::checkApplicationLoan);

        assertEquals("Invalid status to create request", exception.getMessage());
    }

    @Test
    void shouldThrowException_WhenTotalMonthsIsGreaterThanAmount () {
        Loan domain = Loan.LoanBuilder
                .aLoan()
                .document("123")
                .amount(BigDecimal.valueOf(10))
                .period(1, 6)
                .loanTypeCode(1L)
                .build();

        AmountLoanIsNotEnoughDomainException exception = assertThrows(AmountLoanIsNotEnoughDomainException.class, domain::checkApplicationLoan);

        assertEquals("Amount is not enough, Total months is greater than amount loan", exception.getMessage());
    }


    @Test
    void shouldReturnLoanWithSuccessful_whenCheckApplication () {
        Loan domain = Loan.LoanBuilder
                .aLoan()
                .document("123")
                .amount(BigDecimal.valueOf(10000))
                .period(1, 6)
                .loanTypeCode(1L)
                .build();

        domain.checkApplicationLoan();

        assertNotNull(domain);
        assertInstanceOf(Loan.class, domain);

        assertNull(domain.getId());
        // assertEquals(LoanStatus.PENDING, domain.getLoanStatus());
        assertNull(domain.getLoanStatus());
        assertEquals(18, domain.getPeriod().calculateTotalMonths());

        assertEquals(0, domain.getUncommittedEvents().size());
    }


    @Test void shouldBeSuccess_whenLoadCustomerAndLoanType () {
        Loan domain = Loan.LoanBuilder
                .aLoan()
                .document("123")
                .amount(BigDecimal.valueOf(100000))
                .period(1, 6)
                .loanTypeCode(1L)
                .build();

        domain.checkApplicationLoan();

        assertEquals(0, domain.getUncommittedEvents().size());

        CustomerRead customerRead =CustomerRead.builder()
                .email("example@gmail.com")
                .name("example")
                .document("123")
                .baseSalary(BigDecimal.valueOf(20))
                .build();

        domain.loadCustomer(customerRead);
        assertEquals(0, domain.getUncommittedEvents().size());

        LoanType loanType = LoanType.LoanTypeBuilder.aLoanType()
                .description("Example")
                .id(1L)
                .interestRate(1.0D)
                .build();

        domain.loadLoanType(loanType);
        assertEquals(0, domain.getUncommittedEvents().size());
    }

    @Test void shouldHavePendingStatus_whenMarkAsPending_afterCheckAndLoading () {
        Loan domain = Loan.LoanBuilder
                .aLoan()
                .document("123")
                .amount(BigDecimal.valueOf(100000))
                .period(1, 6)
                .loanTypeCode(1L)
                .build();

        domain.checkApplicationLoan();

        assertEquals(0, domain.getUncommittedEvents().size());

        CustomerRead customerRead =CustomerRead.builder()
                .email("example@gmail.com")
                .name("example")
                .document("123")
                .baseSalary(BigDecimal.valueOf(20))
                .build();

        domain.loadCustomer(customerRead);
        assertEquals(0, domain.getUncommittedEvents().size());

        LoanType loanType = LoanType.LoanTypeBuilder.aLoanType()
                .description("Example")
                .id(1L)
                .interestRate(1.0D)
                .build();

        domain.loadLoanType(loanType);
        assertEquals(0, domain.getUncommittedEvents().size());

        domain.markAsPending();
        assertEquals(1, domain.getUncommittedEvents().size());

        domain.clearUncommittedEvents();
        assertEquals(0, domain.getUncommittedEvents().size());
    }

}
