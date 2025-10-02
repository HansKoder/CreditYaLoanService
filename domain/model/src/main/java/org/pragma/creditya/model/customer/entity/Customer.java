package org.pragma.creditya.model.customer.entity;

import lombok.Getter;
import lombok.Setter;
import org.pragma.creditya.model.customer.valueobject.CustomerId;
import org.pragma.creditya.model.customer.valueobject.Document;
import org.pragma.creditya.model.shared.domain.model.entity.AggregateRoot;
import org.pragma.creditya.model.shared.domain.model.valueobject.Amount;

@Getter
@Setter
public class Customer extends AggregateRoot<CustomerId> {
    private String email;
    private String name;
    private Document document;
    private Amount baseSalary;

    private Customer(CustomerBuilder builder) {
        super.setId(builder.id);
        document = builder.document;
        email = builder.email;
        name = builder.name;
        baseSalary = builder.baseSalary;
    }

    public static final class CustomerBuilder {
        private Amount baseSalary;
        private String email;
        private String name;
        private Document document;
        private CustomerId id;

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

        public CustomerBuilder document(Document document) {
            this.document = document;
            return this;
        }

        public CustomerBuilder id(CustomerId id) {
            this.id = id;
            return this;
        }

        public Customer build() {
            return new Customer(this);
        }
    }
}
