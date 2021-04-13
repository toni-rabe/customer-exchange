package com.trabe.insso.customers.domain.customer;

import java.util.UUID;

public class CustomerCaseNotFoundException extends RuntimeException {

    public CustomerCaseNotFoundException(UUID customerCaseId) {
        super(String.format("Customer Case: %s not found", customerCaseId));
    }
}
