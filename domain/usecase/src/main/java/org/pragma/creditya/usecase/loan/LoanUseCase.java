package org.pragma.creditya.usecase.loan;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loan.exception.DocumentDoestExistDomainException;
import org.pragma.creditya.model.loan.gateways.LoanRepository;
import org.pragma.creditya.model.loan.gateways.UserClient;
import org.pragma.creditya.usecase.loan.command.CreateRequestLoanCommand;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LoanUseCase implements ILoanUseCase {

    private final LoanRepository loanRepository;
    private final UserClient userClient;

    @Override
    public Mono<Loan> createRequestLoan(CreateRequestLoanCommand cmd) {
        return Mono.fromCallable(() -> checkLoan(cmd))
                //.flatMap(this::checkDocument)
                .flatMap(loanRepository::save);
    }

    private Mono<Loan> checkDocument (Loan entity) {
        return userClient.exitByDocument(entity.getDocument().value())
                .flatMap(exist -> {
                    if (exist) return Mono.just(entity);

                    return Mono.error(new DocumentDoestExistDomainException("Document " + entity.getDocument().value() + " does not exist in the DB"));
                });
    }

    private Loan checkLoan (CreateRequestLoanCommand cmd) {
        return Loan.createRequestLoan(cmd.document(), cmd.amount(), cmd.period());
    }
}
