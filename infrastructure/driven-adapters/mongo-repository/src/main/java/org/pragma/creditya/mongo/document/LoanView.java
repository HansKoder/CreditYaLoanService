package org.pragma.creditya.mongo.document;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.math.BigDecimal;
import java.util.UUID;

@Document(collection = "loan_view")
@Data
@Builder
public class LoanView {

    @MongoId
    private String id;
    private UUID loanId;
    private String document;


}
