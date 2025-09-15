package org.pragma.creditya.model.customer.entity;

import lombok.Builder;
import lombok.Getter;
import org.pragma.creditya.model.customer.valueobject.CustomerId;
import org.pragma.creditya.model.shared.domain.model.entity.BaseEntity;

@Builder
@Getter
public class Customer extends BaseEntity<CustomerId> {
    private String email;
    private String name;
    private String document;
}
