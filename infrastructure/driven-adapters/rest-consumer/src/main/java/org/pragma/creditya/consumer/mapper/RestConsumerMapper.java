package org.pragma.creditya.consumer.mapper;

import org.pragma.creditya.consumer.response.GetCustomerByDocumentResponse;
import org.pragma.creditya.model.loan.entity.CustomerRead;

public class RestConsumerMapper {
    public static CustomerRead toCustomerRead (GetCustomerByDocumentResponse requestResponse) {
        return CustomerRead.builder()
                .name(requestResponse.name())
                .document(requestResponse.document())
                .email(requestResponse.email())
                .baseSalary(requestResponse.baseSalary())
                .build();
    }
}
