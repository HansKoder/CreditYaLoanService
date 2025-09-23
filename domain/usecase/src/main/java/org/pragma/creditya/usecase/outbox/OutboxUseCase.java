package org.pragma.creditya.usecase.outbox;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.model.loan.event.LoanApprovalStatisticsUpdatedEvent;
import org.pragma.creditya.model.loan.event.LoanEvent;
import org.pragma.creditya.model.loan.event.LoanResolutionCustomerNotifiedEvent;
import org.pragma.creditya.model.loan.gateways.OutboxRepository;
import org.pragma.creditya.usecase.notification.INotificationUseCase;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class OutboxUseCase implements IOutboxUseCase {

    private final OutboxRepository outboxRepository;
    private final INotificationUseCase notificationUseCase;

    @Override
    public Mono<Void> execute(LoanEvent event) {
        return Mono.just(event)
                .flatMap(this::handleEvent)
                .then();
    }

    private Mono<Void> handleEvent(LoanEvent event) {
        if (event instanceof LoanResolutionCustomerNotifiedEvent notifiedEvent) {
            return notification(notifiedEvent);
        } else if (event instanceof LoanApprovalStatisticsUpdatedEvent statsEvent) {
            return report(statsEvent);
        }

        return Mono.empty();
    }

    private Mono<Void> notification(LoanResolutionCustomerNotifiedEvent event) {
        return notificationUseCase.sendNotification(event);
    }

    private Mono<Void> report(LoanApprovalStatisticsUpdatedEvent event) {
        return Mono.empty();
    }
}
