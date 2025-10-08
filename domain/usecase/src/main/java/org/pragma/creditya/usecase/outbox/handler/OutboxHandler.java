package org.pragma.creditya.usecase.outbox.handler;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loan.event.LoanEvent;
import org.pragma.creditya.usecase.outbox.gateway.OutboxRepository;
import org.pragma.creditya.usecase.outbox.LoanOutboxMessage;
import org.pragma.creditya.usecase.outbox.OutboxHelper;
import org.pragma.creditya.usecase.outbox.payload.OutboxPayload;
import org.pragma.creditya.usecase.outbox.strategy.OutboxStrategy;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class OutboxHandler implements IOutboxHandler {

    private final OutboxRepository outboxRepository;
    private final List<OutboxStrategy> strategies;

    @Override
    public Mono<Void> execute(Loan domain) {
        return Flux.fromIterable(domain.getUncommittedEvents())
                .log()
                .map(event -> {
                    System.out.println("event:: " + event);
                    return event;
                })
                .flatMap(event -> processOutbox(domain, event))
                .then();
    }

    private Flux<LoanOutboxMessage> processOutbox(Loan domain, LoanEvent event) {
        System.out.println("[domain.outbox] (processOutbox) payload=[ domain:{" + domain + "}, event:{" + event+ "} ]");

        return Flux.fromStream(strategies.stream().filter(strategy -> strategy.apply(event.getPayload())))
                .flatMap(strategy -> strategy.handler(domain, event))
                .flatMap(payload -> processPayload(event, payload));
    }

    private Mono<LoanOutboxMessage> processPayload(LoanEvent event, OutboxPayload payload) {
        System.out.println("[domain.outbox] (processPayload) payload=[ event:{" + event + "}, payload:{" + payload+ "} ]");
        LoanOutboxMessage outboxMessage = OutboxHelper.toOutboxMessage(event, payload);

        return outboxRepository.saveOutboxMessage(outboxMessage, payload)
                .thenReturn(outboxMessage);
    }


}
