package org.pragma.creditya.consumer.customer.rest.response;

import java.math.BigDecimal;

public record CustomerResponse(
        String customerId,
        String document,
        String email,
        BigDecimal baseSalary,
        String name
) { }
