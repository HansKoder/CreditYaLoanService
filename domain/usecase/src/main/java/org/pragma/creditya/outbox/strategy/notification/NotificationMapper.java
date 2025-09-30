package org.pragma.creditya.outbox.strategy.notification;

import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loan.entity.CustomerRead;
import org.pragma.creditya.model.loan.event.LoanEvent;
import org.pragma.creditya.model.loan.valueobject.LoanStatus;
import org.pragma.creditya.outbox.payload.NotificationOutboxPayload;

public class NotificationMapper {
    public static NotificationOutboxPayload toPayload (Loan domain, LoanEvent event, CustomerRead customer) {
        System.out.println("[use_case.outbox.notification.mapper] (toPayload) payload=[ domain:{" + domain + "}, event:{"+ event+ "}]");
        NotificationOutboxPayload payload = NotificationOutboxPayload.builder()
                .type("EMAIL")
                .subject("Loan Decision")
                .destination(customer.getEmail())
                .message(extractMessage(customer, domain, event))
                .build();

        System.out.println("[use_case.outbox.notification.mapper] (toPayload) return NotificationOutboxPayload, payload=[ payload:{" + payload + "}, event:{"+ event+ "}]");
        return payload;
    }

    private static String extractMessage (CustomerRead customer, Loan domain, LoanEvent event) {
        System.out.println("[use_case.outbox.notification.mapper] (extractMessage) payload=[ domain:{" + domain + "}, event:{"+ event+ "}]");

        String greeting = "Hello, " + customer.getName();
        StringBuilder message = new StringBuilder(greeting).append("\n\n");

        if (domain.getLoanStatus().equals(LoanStatus.APPROVED)) {
            message.append("Congratulations! Your loan with the code ")
                    .append(event.getAggregateId().toString())
                    .append(" was approved successfully.");
        } else if (domain.getLoanStatus().equals(LoanStatus.REJECTED)) {
            message.append("Sorry, your loan with the code ")
                    .append(event.getAggregateId().toString())
                    .append(" was rejected.");

            if (domain.getReason() != null && !domain.getReason().isBlank()) {
                message.append(" Reason: ").append(domain.getReason()).append(".");
            }
        }

        message.append("\n\nBest regards,\nCreditYa Team..");
        message.append("\n\nIf you have any questions, please do not hesitate to contact our support team for further details or assistance.");

        System.out.println("[use_case.outbox.notification.mapper] (extractMessage) message was build payload=[ message:{" + message.toString() + "}]");

        return message.toString();
    }

}
