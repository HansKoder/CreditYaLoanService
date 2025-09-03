package org.pragma.creditya.model.loan.valueobject;

import lombok.ToString;
import org.pragma.creditya.model.shared.domain.model.valueobject.BaseId;

import java.util.UUID;

@ToString
public class LoanId extends BaseId<UUID> {
    public LoanId(UUID value) {
        super(value);
    }
}
