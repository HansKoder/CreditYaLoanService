package org.pragma.creditya.model.customer.valueobject;

import org.pragma.creditya.model.shared.domain.model.valueobject.BaseId;

import java.util.UUID;

// Pending -> document or customerId
public class CustomerId extends BaseId<UUID> {
    public CustomerId(UUID value) {
        super(value);
    }
}
