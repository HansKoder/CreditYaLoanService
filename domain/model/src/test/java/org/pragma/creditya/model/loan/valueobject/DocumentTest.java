package org.pragma.creditya.model.loan.valueobject;

import org.junit.jupiter.api.Test;
import org.pragma.creditya.model.customer.valueobject.Document;
import org.pragma.creditya.model.loan.exception.LoanDomainException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DocumentTest {

    @Test
    void shouldThrowExceptionWhenDocumentCustomerIsNull () {
        LoanDomainException exception = assertThrows(LoanDomainException.class, () -> {
            new Document(null);
        });

        assertEquals("Document must be mandatory", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenDocumentCustomerIsEmpty () {
        LoanDomainException exception = assertThrows(LoanDomainException.class, () -> {
            new Document(" ");
        });

        assertEquals("Document must be mandatory", exception.getMessage());
    }

    @Test
    void shouldCreateDocumentWithSuccessful () {
        Document document = new Document("123");

        assertEquals("123", document.getValue());
    }

}
