package org.pragma.creditya.usecase.notification;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.model.loan.event.LoanResolutionCustomerNotifiedEvent;
import org.pragma.creditya.model.loan.gateways.SQSRepository;
import org.pragma.creditya.usecase.notification.command.SendNotificationCommand;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class NotificationUseCase implements INotificationUseCase{

    private final SQSRepository sqsRepository;

    @Override
    public Mono<Void> sendNotification(LoanResolutionCustomerNotifiedEvent event) {
        return Mono.fromCallable(this::build)
                .flatMap(sqsRepository::sendMessage);
    }

    private SendNotificationCommand build () {
        return new SendNotificationCommand("EMAIL", "doe@gmail.com", "Loan was approved", "Loan Approved");
    }
}
