package org.pragma.creditya.consumer;


import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.pragma.creditya.consumer.customer.RestConsumer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;
import reactor.util.context.Context;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;


class RestConsumerTest {

    private static RestConsumer restConsumer;

    private static MockWebServer mockBackEnd;

    private final String TOKEN_EXAMPLE = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyb2JpbkBjcmVkaXQuY29tIiwicm9sZXMiOlt7ImF1dGhvcml0eSI6IkNVU1RPTUVSIn1dLCJpYXQiOjE3NTczOTMzMDEsImV4cCI6MTc1NzM5NjkwMX0.NOCsmPZbJsKITMwYkQX07PYUgZKLFRyFYlwSY7P9D0k";

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
        var webClient = WebClient.builder().baseUrl(mockBackEnd.url("/").toString()).build();
        restConsumer = new RestConsumer(webClient);
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @Test
    @DisplayName("Should get customer with successful")
    void shouldBeSuccess_becauseCustomerExist() {

        mockBackEnd.enqueue(new MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(HttpStatus.OK.value())
                .setBody("{\"name\" : \"jhon\", \"email\" : \"example@gmail.com\", \"baseSalary\" : 12000, \"document\" : \"123\"   }"));

        Context context = Context.of("token", TOKEN_EXAMPLE);

        var response = restConsumer.verifyOwnershipCustomer("123", "example@gmail.com");

        StepVerifier.create(response.contextWrite(context))
                .expectNextMatches(objectResponse -> objectResponse.getDocument().equals("123"))
                .verifyComplete();
    }

    @Test
    @DisplayName("Should Throw Exception when customer is not authorized")
    void shouldThrowException_whenCustomerIsInvalid() {

        mockBackEnd.enqueue(new MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(HttpStatus.UNAUTHORIZED.value())
                .setBody("{\"status\" : 403, \"error\" : \"Invalid Operation\"}"));

        Context context = Context.of("token", TOKEN_EXAMPLE);

        var response = restConsumer.verifyOwnershipCustomer("123", "example@gmail.com");

        StepVerifier.create(response.contextWrite(context))
                .expectErrorSatisfies(err ->
                    assertEquals(err.getMessage(), "Invalid Operation")
                )
                .verify();
    }

}