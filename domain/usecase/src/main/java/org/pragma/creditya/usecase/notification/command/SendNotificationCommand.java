package org.pragma.creditya.usecase.notification.command;

public record SendNotificationCommand (
    String type,
    String destination,
    String message,
    String subject
) { }
