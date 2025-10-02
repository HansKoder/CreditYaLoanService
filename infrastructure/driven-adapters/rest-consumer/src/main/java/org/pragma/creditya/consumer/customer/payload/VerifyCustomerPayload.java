package org.pragma.creditya.consumer.customer.payload;

public record VerifyCustomerPayload(String document, String email, String token) {
}
