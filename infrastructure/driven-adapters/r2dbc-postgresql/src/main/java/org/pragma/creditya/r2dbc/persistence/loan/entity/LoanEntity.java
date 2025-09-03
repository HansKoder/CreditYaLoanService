package org.pragma.creditya.r2dbc.persistence.loan.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Table(name = "loans", schema = "public")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LoanEntity {

    @Id
    @Column(value = "loan_id")
    @GeneratedValue(strategy = GenerationType.UUID)
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
