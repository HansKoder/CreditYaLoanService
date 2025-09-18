package org.pragma.creditya.consumer.response;

import java.math.BigDecimal;

public record GetCustomerByDocumentResponse(
        String document,
        String email,
        BigDecimal baseSalary,
        String name
) { }
