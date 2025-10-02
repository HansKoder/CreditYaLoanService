package org.pragma.creditya.usecase.outbox.strategy.notification;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loan.event.LoanEvent;
import org.pragma.creditya.model.loan.event.LoanResolutionApprovedEvent;
import org.pragma.creditya.model.loan.event.LoanResolutionRejectedEvent;
import org.pragma.creditya.model.customer.gateway.CustomerRepository;
import org.pragma.creditya.usecase.outbox.payload.OutboxPayload;
import org.pragma.creditya.usecase.outbox.strategy.OutboxStrategy;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class NotificationHandler implements OutboxStrategy {

    private final CustomerRepository customerClient;

    @Override
    public boolean apply(LoanEvent event) {
        System.out.println("[use_case.outbox.strategy] (apply) payload=[ event:{" + event + "}]");
        return event instanceof LoanResolutionApprovedEvent || event instanceof LoanResolutionRejectedEvent;
    }

    @Override
    public Mono<OutboxPayload> handler(Loan domain, LoanEvent event) {
        System.out.println("[use_case.outbox.strategy] (handler) payload=[ event:{" + event + "}]");
        return customerClient.getCustomerByDocument(domain.getDocument().value())
                .map(customer -> NotificationMapper.toPayload(domain, event, customer));

    }

}
