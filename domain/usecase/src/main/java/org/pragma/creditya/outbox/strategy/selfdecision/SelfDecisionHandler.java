package org.pragma.creditya.outbox.strategy.selfdecision;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loan.event.ApplicationSubmittedType;
import org.pragma.creditya.model.loan.event.LoanApplicationSubmittedEvent;
import org.pragma.creditya.model.loan.event.LoanEvent;
import org.pragma.creditya.model.loan.gateways.CustomerClient;
import org.pragma.creditya.model.loanread.gateways.LoanReadRepository;
import org.pragma.creditya.outbox.payload.OutboxPayload;
import org.pragma.creditya.outbox.strategy.OutboxStrategy;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class SelfDecisionHandler implements OutboxStrategy {

    private final CustomerClient customerClient;
    private final LoanReadRepository loanReadRepository;

    @Override
    public boolean apply(LoanEvent event) {
        System.out.println("[use_case.outbox.strategy] (apply) payload=[ event:{" + event + "}]");
        return event instanceof LoanApplicationSubmittedEvent
                && checkIsSelfDecision ((LoanApplicationSubmittedEvent) event);
    }

    private boolean checkIsSelfDecision (LoanApplicationSubmittedEvent submittedEvent) {
        return submittedEvent
                .getTypeSubmitted()
                .equals(ApplicationSubmittedType.SELF_DECISION);
    }

    @Override
    public Mono<OutboxPayload> handler(Loan domain, LoanEvent event) {
        System.out.println("[use_case.outbox.strategy] (handler) payload=[ event:{" + event + "}]");

        return Mono.empty();
        /*
        return customerClient.getCustomerByDocument(domain.getDocument().value())
                .flatMap(customer -> loanReadRepository.getActiveDebts(customer.getDocument()))

         */
    }
}
