package org.pragma.creditya.r2dbc.persistence.loan.entity;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Table(name = "loans", schema = "public")
@Getter
@Setter
@ToString
public class LoanEntity {

    @Id
    @Column(value = "loan_id")
    private UUID loanId;

    private String document;

    private LocalDate period;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private LoanStatusEntity status;

}
