package org.pragma.creditya.config;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.pragma.creditya.model.loan.gateways.CustomerClient;
import org.pragma.creditya.model.loan.gateways.LoanRepository;
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
        public LoanRepository repository () {
            return Mockito.mock(LoanRepository.class);
        }

        @Bean
        public CustomerClient client () {
            return Mockito.mock(CustomerClient.class);
        }

    }

    static class MyUseCase {
        public String execute() {
            return "MyUseCase Test";
        }
    }
}