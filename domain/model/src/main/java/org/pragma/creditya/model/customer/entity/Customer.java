package org.pragma.creditya.model.customer.entity;

import lombok.Getter;
import lombok.Setter;
import org.pragma.creditya.model.customer.valueobject.CustomerId;
import org.pragma.creditya.model.customer.valueobject.Document;
import org.pragma.creditya.model.shared.domain.model.entity.AggregateRoot;
import org.pragma.creditya.model.shared.domain.model.valueobject.Amount;

@Getter
@Setter
public class Customer extends AggregateRoot<Document> {
    private String email;
    private String name;
    private Amount baseSalary;

    private Customer(CustomerBuilder builder) {
        super.setId(builder.id);
        email = builder.email;
        name = builder.name;
        baseSalary = builder.baseSalary;
    }

    public static final class CustomerBuilder {
        private Amount baseSalary;
        private String email;
        private String name;
        private Document id;

        private CustomerBuilder() {
        }

        public static CustomerBuilder aCustomer() {
            return new CustomerBuilder();
        }

        public CustomerBuilder baseSalary(Amount baseSalary) {
            this.baseSalary = baseSalary;
            return this;
        }

        public CustomerBuilder email(String email) {
            this.email = email;
            return this;
        }

        public CustomerBuilder name(String name) {
            this.name = name;
            return this;
        }

        public CustomerBuilder id(Document document) {
            this.id = document;
            return this;
        }

        public Customer build() {
            return new Customer(this);
        }
    }
}
