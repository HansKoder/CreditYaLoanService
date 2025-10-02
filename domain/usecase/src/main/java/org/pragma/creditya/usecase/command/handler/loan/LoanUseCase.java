package org.pragma.creditya.usecase.command.handler.loan;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loan.event.LoanEvent;
import org.pragma.creditya.model.loan.exception.CustomerIsNotAllowedDomainException;
import org.pragma.creditya.model.loan.exception.DocumentNotFoundDomainException;
import org.pragma.creditya.model.customer.gateway.CustomerRepository;
import org.pragma.creditya.model.loan.gateways.UserInfoRepository;
import org.pragma.creditya.usecase.command.CreateApplicationLoanCommand;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class LoanUseCase implements ILoanUseCase {

    private final CustomerRepository userClient;
    private final UserInfoRepository userInfoRepository;

    @Override
    public Mono<Loan> checkApplication(CreateApplicationLoanCommand cmd) {
        return Mono.fromCallable(() -> checkApplicationLoan(cmd));
    }

    @Override
    public Mono<Loan> verifyOwnershipCustomer(Loan loan) {
        return  userInfoRepository.getUsername()
                .flatMap(email -> userClient.verifyOwnershipCustomer(loan.getDocument().value(), email))
                .flatMap(result -> {
                    if (result) return Mono.just(loan);

                    return Mono.error(new CustomerIsNotAllowedDomainException("Customer is not allowed to submitted this Loan"));
                });
    }

    @Override
    public Mono<Loan> markAsPending(Loan loan) {
        return Mono.fromCallable(() -> {
            loan.markAsPending();
            return loan;
        });
    }

    @Override
    public Mono<Loan> rehydrate(List<LoanEvent> events) {
        return Mono.just(Loan.rehydrate(events));
    }

    @Override
    public Mono<Loan> loadUsername(Loan loan) {
        return userInfoRepository.getUsername()
                .map(username -> {
                    loan.loadAuthorResolutionLoan(username);
                    return loan;
                });
    }

    @Override
    public Mono<Loan> approvedLoan(Loan loan, String reason) {
        return Mono.fromCallable(() -> {
           loan.checkApprovedLoan(reason);
           return loan;
        });
    }

    @Override
    public Mono<Loan> rejectedLoan(Loan loan, String reason) {
        return Mono.fromCallable(() -> {
            loan.checkRejectedLoan(reason);
            return loan;
        });
    }

    private Loan checkApplicationLoan (CreateApplicationLoanCommand cmd) {
        Loan domain = Loan.LoanBuilder.aLoan()
                .document(cmd.document())
                .amount(cmd.amount())
                .period(cmd.year(), cmd.month())
                .loanTypeCode(cmd.loanTypeId())
                .build();

        domain.checkApplicationLoan();

        return domain;
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


}
