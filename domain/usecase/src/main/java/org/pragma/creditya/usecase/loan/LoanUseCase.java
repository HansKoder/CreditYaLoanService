package org.pragma.creditya.usecase.loan;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loan.exception.DocumentNotFoundDomainException;
import org.pragma.creditya.model.loan.exception.LoanTypeNotFoundDomainException;
import org.pragma.creditya.model.loan.gateways.LoanRepository;
import org.pragma.creditya.model.loan.gateways.LoanTypeRepository;
import org.pragma.creditya.model.loan.gateways.CustomerClient;
import org.pragma.creditya.usecase.loan.command.CreateRequestLoanCommand;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LoanUseCase implements ILoanUseCase {

    private final LoanRepository loanRepository;
    private final CustomerClient userClient;
    private final LoanTypeRepository loanTypeRepository;

    @Override
    public Mono<Loan> createRequestLoan(CreateRequestLoanCommand cmd) {
        return Mono.fromCallable(() -> checkLoan(cmd))
                .flatMap(this::checkDocument)
                .flatMap(this::checkLoanType)
                .flatMap(loanRepository::save);
    }

    private Mono<Loan> checkDocument (Loan entity) {
        return userClient.exitByDocument(entity.getDocument().value())
                .flatMap(exist -> {
                    if (exist)
                        return Mono.just(entity);

                    String err = String.format("Document %s does not exist, you need to check", entity.getDocument().value());
                    return Mono.error(new DocumentNotFoundDomainException(err));
                });
    }

    private Mono<Loan> checkLoanType (Loan entity) {
        return loanTypeRepository.existLoanType(entity.getLoanType().code())
                .flatMap(exist -> {
                    if (exist)
                        return Mono.just(entity);

                    String err = String.format("Type Loan code %s does not exist, you need to check", entity.getLoanType().code());
                    return Mono.error(new LoanTypeNotFoundDomainException(err));
                });
    }

    private Loan checkLoan (CreateRequestLoanCommand cmd) {
        Loan domain = Loan.LoanBuilder.aLoan()
                .document(cmd.document())
                .amount(cmd.amount())
                .period(cmd.year(), cmd.month())
                .loanType(cmd.loanTypeId())
                .build();

        domain.createRequestLoan();

        return domain;
    }
}
