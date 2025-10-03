package org.pragma.creditya.config;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.pragma.creditya.model.loan.bus.EventBus;
import org.pragma.creditya.model.customer.gateway.CustomerRepository;
import org.pragma.creditya.model.loan.gateways.EventStoreRepository;
import org.pragma.creditya.model.loan.gateways.UserInfoRepository;
import org.pragma.creditya.model.loantype.gateways.LoanTypeRepository;
import org.pragma.creditya.usecase.query.handler.loan.LoanQuery;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UseCasesConfigTest {

    @Test
    void testUseCaseBeansExist() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class)) {
            String[] beanNames = context.getBeanDefinitionNames();

            boolean useCaseBeanFound = false;
            for (String beanName : beanNames) {
                if (beanName.endsWith("UseCase")) {
                    useCaseBeanFound = true;
                    break;
                }
            }

            assertTrue(useCaseBeanFound, "No beans ending with 'Use Case' were found");
        }
    }

    @Configuration
    @Import(UseCasesConfig.class)
    static class TestConfig {

        @Bean
        public MyUseCase myUseCase() {
            return new MyUseCase();
        }

        @Bean
        public CustomerRepository client () {
            return Mockito.mock(CustomerRepository.class);
        }

        @Bean
        public LoanTypeRepository loanType () {
            return Mockito.mock(LoanTypeRepository.class);
        }

        @Bean
        public EventStoreRepository eventStoreRepository () { return Mockito.mock(EventStoreRepository.class); }

        @Bean
        public UserInfoRepository userInfoRepository () {
            return Mockito.mock(UserInfoRepository.class);
        }

        @Bean
        public LoanQuery loanReadUseCase () {
            return Mockito.mock(LoanQuery.class);
        }

        @Bean
        public EventBus eventBus () { return Mockito.mock(EventBus.class); }

    }

    static class MyUseCase {
        public String execute() {
            return "MyUseCase Test";
        }
    }
}