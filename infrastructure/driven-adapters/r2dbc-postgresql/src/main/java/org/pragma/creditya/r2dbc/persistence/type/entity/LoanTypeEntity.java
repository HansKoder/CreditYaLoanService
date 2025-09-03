package org.pragma.creditya.r2dbc.persistence.type.entity;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Builder
@Data
@Table(value = "loan_types")
public class LoanTypeEntity {

    @Id
    private Long id;

    private String description;

    @Column(value = "interest_rate")
    private Double interestRate;

}
