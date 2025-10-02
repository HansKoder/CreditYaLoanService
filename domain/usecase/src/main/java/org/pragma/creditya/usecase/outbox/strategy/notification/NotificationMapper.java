package org.pragma.creditya.usecase.outbox.strategy.notification;

import org.pragma.creditya.model.loan.Loan;
import org.pragma.creditya.model.loan.entity.CustomerRead;
import org.pragma.creditya.model.loan.event.LoanEvent;
import org.pragma.creditya.model.loan.valueobject.LoanStatus;
import org.pragma.creditya.usecase.outbox.payload.NotificationOutboxPayload;

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

    private static void greetingMessage (StringBuilder stringBuilder, CustomerRead customer) {
        stringBuilder.append("Hello, Dear ")
                .append(customer.getName().isEmpty() ? "Customer" : customer.getName())
                .append("\n\n");
    }

    private static void approvedMessage (StringBuilder message, LoanEvent event) {
        message.append("Congratulations! Your loan with the code ")
                .append(event.getAggregateId().toString())
                .append(" was approved successfully. \n\n");
    }

    private static void rejectedMessage (StringBuilder message, LoanEvent event, Loan domain) {
        message.append("Sorry, your loan with the code ")
                .append(event.getAggregateId().toString())
                .append(" was rejected. \n\n");

        if (domain.getReason() != null && !domain.getReason().isBlank()) {
            message.append("Reason: ").append(domain.getReason())
                    .append(".")
                    .append("\n\n");
        }
    }

    private static void footer (StringBuilder message) {
        message.append("Best regards\nCreditYa Team\n\n")
                .append("If you have any questions, please do not hesitate to contact our support team for further details or assistance.");

    }

    private static String extractMessage (CustomerRead customer, Loan domain, LoanEvent event) {
        System.out.println("[use_case.outbox.notification.mapper] (extractMessage) payload=[ domain:{" + domain + "}, event:{"+ event+ "}]");

        StringBuilder message = new StringBuilder();
        greetingMessage(message, customer);

        if (domain.getLoanStatus().equals(LoanStatus.APPROVED))
            approvedMessage(message, event);

        if (domain.getLoanStatus().equals(LoanStatus.REJECTED))
            rejectedMessage(message, event, domain);

        footer(message);

        System.out.println("[use_case.outbox.notification.mapper] (extractMessage) message was build payload=[ message:{" + message.toString() + "}]");

        return message.toString();
    }

}
