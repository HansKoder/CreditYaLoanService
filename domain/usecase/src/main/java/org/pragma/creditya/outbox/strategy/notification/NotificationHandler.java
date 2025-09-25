package org.pragma.creditya.outbox.strategy.notification;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loan.event.LoanEvent;
import org.pragma.creditya.model.loan.event.LoanResolutionApprovedEvent;
import org.pragma.creditya.model.loan.event.LoanResolutionRejectedEvent;
import org.pragma.creditya.model.loan.gateways.CustomerClient;
import org.pragma.creditya.outbox.payload.NotificationOutboxPayload;
import org.pragma.creditya.outbox.strategy.OutboxStrategy;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class NotificationHandler implements OutboxStrategy {

    private final CustomerClient customerClient;

    @Override
    public boolean apply(LoanEvent event) {
        System.out.println("[use_case.outbox.strategy] (apply) payload=[ event:{" + event + "}]");
        return event instanceof LoanResolutionApprovedEvent || event instanceof LoanResolutionRejectedEvent;
    }

    @Override
    public Mono<NotificationOutboxPayload> handler(Loan domain, LoanEvent event) {
        System.out.println("[use_case.outbox.strategy] (handler) payload=[ event:{" + event + "}]");
        return customerClient.getCustomerByDocument(domain.getDocument().value())
                .map(customer -> NotificationMapper.toPayload(domain, event, customer));

    }

}
