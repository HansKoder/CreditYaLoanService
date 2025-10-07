package org.pragma.creditya.r2dbc.persistence.loantype.entity;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.pragma.creditya.r2dbc.persistence.outbox.entity.OutboxStatus;
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

    @Enumerated(EnumType.STRING)
    @Column(value = "resolution_type")
    private ResolutionTypeEntity resolutionType;

}
