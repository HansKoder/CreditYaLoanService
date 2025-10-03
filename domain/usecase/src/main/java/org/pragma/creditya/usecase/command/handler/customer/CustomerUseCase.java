package org.pragma.creditya.usecase.command.handler.customer;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.model.customer.entity.Customer;
import org.pragma.creditya.model.customer.exception.CustomerIsNotIdentifiedDomainException;
import org.pragma.creditya.model.customer.gateway.CustomerRepository;
import org.pragma.creditya.model.customer.valueobject.Document;
import org.pragma.creditya.model.loan.gateways.UserInfoRepository;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CustomerUseCase implements ICustomerUseCase{

    private final CustomerRepository customerRepository;
    private final UserInfoRepository userInfoRepository;

    @Override
    public Mono<Void> verifyMyIdentity(Document document) {
        return userInfoRepository
                .getUsername()
                .flatMap(username -> customerRepository.verifyCustomerByDocumentAndEmail(document, username))
                .flatMap(result -> {
                    if (result)
                        return Mono.empty();

                    return Mono.error(
                            new CustomerIsNotIdentifiedDomainException("Customer with the document " + document.getValue() + " is not identified"));
                });
    }

    @Override
    public Mono<Customer> getCustomer(Document document) {
        return customerRepository.getCustomerByDocument(document);
    }
}
