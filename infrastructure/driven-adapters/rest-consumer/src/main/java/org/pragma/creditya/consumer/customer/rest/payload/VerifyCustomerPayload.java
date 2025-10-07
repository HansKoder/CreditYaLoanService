package org.pragma.creditya.consumer.customer.rest.payload;

public record VerifyCustomerPayload(String document, String email, String token) {
}
