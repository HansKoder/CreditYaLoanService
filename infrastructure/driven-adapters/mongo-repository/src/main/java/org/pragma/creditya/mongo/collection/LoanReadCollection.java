package org.pragma.creditya.mongo.collection;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.math.BigDecimal;
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

}
