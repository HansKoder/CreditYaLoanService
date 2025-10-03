package org.pragma.creditya.consumer.customer.mapper;

import org.pragma.creditya.consumer.customer.response.CustomerResponse;
import org.pragma.creditya.model.customer.entity.Customer;
import org.pragma.creditya.model.customer.valueobject.CustomerId;
import org.pragma.creditya.model.customer.valueobject.Document;
import org.pragma.creditya.model.shared.domain.model.valueobject.Amount;

import java.util.UUID;

public class RestConsumerMapper {
    public static Customer toEntity (CustomerResponse requestResponse) {
        return Customer.CustomerBuilder.aCustomer()
                .name(requestResponse.name())
                .id(new Document(requestResponse.document()))
                .email(requestResponse.email())
                .baseSalary(new Amount(requestResponse.baseSalary()))
                .build();
    }
}
