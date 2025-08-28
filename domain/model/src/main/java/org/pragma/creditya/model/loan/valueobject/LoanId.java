package org.pragma.creditya.model.loan.valueobject;

import org.pragma.creditya.model.shared.domain.model.valueobject.BaseId;

import java.util.UUID;

public class LoanId extends BaseId<UUID> {
    public LoanId(UUID value) {
        super(value);
    }
}
