package org.pragma.creditya.model.loan.entity;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class CustomerRead {

    private String name;
    private String email;
    private String document;
    private BigDecimal baseSalary;

}
