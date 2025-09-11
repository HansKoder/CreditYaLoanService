package org.pragma.creditya.consumer.response;

import java.math.BigDecimal;

public record CustomerResponse (
        String document,
        String email,
        BigDecimal baseSalary,
        String name
) { }
