package org.pragma.creditya.r2dbc.persistence.loantype.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(value = "loan_types")
public class LoanTypeEntity {

    @Id
    private Long id;

    private String description;

    @Column(value = "interest_rate")
    private Double interestRate;

    @Column(value = "auto_decision")
    private Boolean auto;

}
