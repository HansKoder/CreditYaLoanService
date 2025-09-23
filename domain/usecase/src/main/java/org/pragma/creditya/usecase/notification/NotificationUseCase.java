package org.pragma.creditya.usecase.notification;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.model.loan.entity.CustomerRead;
import org.pragma.creditya.model.loan.event.LoanResolutionCustomerNotifiedEvent;
import org.pragma.creditya.model.loan.gateways.CustomerClient;
import org.pragma.creditya.model.loan.gateways.SQSRepository;
import org.pragma.creditya.usecase.notification.command.SendNotificationCommand;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class NotificationUseCase implements INotificationUseCase{

    private final SQSRepository sqsRepository;
    private final CustomerClient customerClient;

    private final String EMAIL = "EMAIL";
    private final String DEFAULT_SUBJECT = "NOTIFICATION LOAN";

    @Override
    public Mono<Void> sendNotification(LoanResolutionCustomerNotifiedEvent event) {
        return  customerClient.getCustomerByDocument(event.getDocument())
                .map(c -> buildMessage(c, event) )
                .flatMap(sqsRepository::sendMessage);
    }

    // Build object for sending to sqs
    private SendNotificationCommand buildMessage (CustomerRead customer, LoanResolutionCustomerNotifiedEvent event) {
        return new SendNotificationCommand(
                EMAIL,
                customer.getEmail(),
                extractMessage(customer, event),
                DEFAULT_SUBJECT
        );
    }

    private String extractMessage (CustomerRead customer, LoanResolutionCustomerNotifiedEvent event) {
        String greeting = "Hello, " + customer.getName();
        StringBuilder message = new StringBuilder(greeting).append("\n\n");

        if ("APPROVED".equalsIgnoreCase(event.getDecision())) {
            message.append("Congratulations! Your loan with the code ")
                    .append(event.getAggregateId().toString())
                    .append(" was approved successfully.");
        } else if ("REJECTED".equalsIgnoreCase(event.getDecision())) {
            message.append("Sorry, your loan with the code ")
                    .append(event.getAggregateId().toString())
                    .append(" was rejected.");

            if (event.getReason() != null && !event.getReason().isBlank()) {
                message.append(" Reason: ").append(event.getReason()).append(".");
            }
        }

        message.append("\n\nBest regards,\nCreditYa Team..");
        message.append("\n\nIf you have any questions, please do not hesitate to contact our support team for further details or assistance.");

        return message.toString();
    }
}
