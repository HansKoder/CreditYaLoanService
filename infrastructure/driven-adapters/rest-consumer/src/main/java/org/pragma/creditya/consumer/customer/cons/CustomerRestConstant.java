package org.pragma.creditya.consumer.customer.cons;

public class CustomerRestConstant {

    private CustomerRestConstant() {}

    // Endpoints
    public final static String CUSTOMER_ENDPOINT_GET_DOCUMENT = "/api/v1/users/document";
    public final static String CUSTOMER_ENDPOINT_VERIFY = "/api/v1/users/verify-ownership-customer";

    // Params
    public final static String CUSTOMER_PARAM_EMAIL = "email";
    public final static String CUSTOMER_PARAM_DOCUMENT = "document";

    // Common
    public final static String BEARER = "Bearer ";

}
