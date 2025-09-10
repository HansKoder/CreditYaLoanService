package org.pragma.creditya.mongo.collection;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.math.BigDecimal;
import java.util.UUID;

@Document(collection = "loanReadCollection")
@Data
@Builder
public class LoanReadCollection {

    @MongoId
    private String id;
    private UUID loanId;
    private String document;
    private BigDecimal amount;

}
