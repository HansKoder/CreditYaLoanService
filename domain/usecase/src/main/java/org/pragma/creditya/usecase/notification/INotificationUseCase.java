package org.pragma.creditya.usecase.notification;

import org.pragma.creditya.model.loan.event.LoanResolutionCustomerNotifiedEvent;
import reactor.core.publisher.Mono;

public interface INotificationUseCase {

    Mono<Void> sendNotification(LoanResolutionCustomerNotifiedEvent event);

}
