package org.pragma.creditya.mongo.collection;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Document(collection = "LoanReadCollection")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class LoanReadCollection {

    @MongoId
    private String id;
    private UUID loanId;
    private String document;
    private BigDecimal amount;
    private Long typeLoan;
    private Integer months;
    private String status;
    private BigDecimal totalMonthlyDebt;
    private Instant timestamp;

    private String name;
    private String email;
    private BigDecimal baseSalary;
    private String loanTypeDescription;
    private Double interestRate;

}
