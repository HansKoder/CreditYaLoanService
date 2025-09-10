package org.pragma.creditya.api.dto.request;

public record GetLoanRequest (
        String status,
        String document,
        int page
) { }
