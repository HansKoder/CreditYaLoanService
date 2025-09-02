package org.pragma.creditya.r2dbc.persistence.loan.entity;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Table(name = "loans", schema = "public")
@Getter
@Setter
@Builder
@ToString
public class LoanEntity {

    @Id
    @Column(value = "loan_id")
    private UUID loanId;

    @Column(value = "loan_type_id")
    private Long loanTypeId;

    private String document;

    @Column(value = "year_period")
    private int year;

    @Column(value = "month_period")
    private int month;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private LoanStatusEntity status;

}
