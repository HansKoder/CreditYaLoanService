package org.pragma.creditya.consumer.customer.response;

import java.math.BigDecimal;

public record CustomerResponse(
        String customerId,
        String document,
        String email,
        BigDecimal baseSalary,
        String name
) { }
