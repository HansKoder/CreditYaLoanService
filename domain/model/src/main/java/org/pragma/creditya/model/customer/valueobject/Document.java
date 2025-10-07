package org.pragma.creditya.model.customer.valueobject;

import org.pragma.creditya.model.customer.exception.CustomerDomainException;
import org.pragma.creditya.model.shared.domain.model.valueobject.BaseId;

public class Document extends BaseId<String> {
    public Document(String value) {
        super(value);

        checkDocument();
    }

    private void checkDocument() {
        if (this.getValue() == null || this.getValue().isBlank())
            throw new CustomerDomainException("Document must be mandatory");
    }
}
