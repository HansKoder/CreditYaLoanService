package org.pragma.creditya.usecase.command.handler.loantype;

import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loantype.LoanType;
import org.pragma.creditya.model.loantype.valueobject.LoanTypeId;
import reactor.core.publisher.Mono;

public interface ILoanTypeUseCase {
    Mono<LoanType> getLoanTypeById (LoanTypeId id);
}
