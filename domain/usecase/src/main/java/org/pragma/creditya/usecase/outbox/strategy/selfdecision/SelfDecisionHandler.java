package org.pragma.creditya.usecase.outbox.strategy.selfdecision;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loan.event.LoanApplicationSubmittedEvent;
import org.pragma.creditya.model.loan.event.LoanEvent;
import org.pragma.creditya.model.customer.gateway.CustomerRepository;
import org.pragma.creditya.model.loanread.gateways.LoanReadRepository;
import org.pragma.creditya.model.loantype.valueobject.ResolutionType;
import org.pragma.creditya.usecase.outbox.payload.OutboxPayload;
import org.pragma.creditya.usecase.outbox.strategy.OutboxStrategy;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class SelfDecisionHandler implements OutboxStrategy {

    private final CustomerRepository customerClient;
    private final LoanReadRepository loanReadRepository;

    @Override
    public boolean apply(LoanEvent event) {
        System.out.println("[use_case.outbox.strategy] (apply) payload=[ event:{" + event + "}]");
        return event instanceof LoanApplicationSubmittedEvent
                && checkIsSelfDecision ((LoanApplicationSubmittedEvent) event);
    }

    private boolean checkIsSelfDecision (LoanApplicationSubmittedEvent submittedEvent) {
        return submittedEvent
                .getResolutionType()
                .equals(ResolutionType.MANUAL_DECISION);
    }

    @Override
    public Mono<OutboxPayload> handler(Loan domain, LoanEvent event) {
        System.out.println("[use_case.outbox.strategy] (handler) payload=[ event:{" + event + "}]");
        return customerClient.getCustomerByDocument(domain.getDocument())
                .zipWith(loanReadRepository.getActiveDebts(domain.getDocument()).collectList())
                .map(tuple -> SelfDecisionMapper.toPayload(domain, tuple.getT2(), tuple.getT1()) );
    }
}
