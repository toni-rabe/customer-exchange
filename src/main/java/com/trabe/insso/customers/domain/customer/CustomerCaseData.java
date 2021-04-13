package com.trabe.insso.customers.domain.customer;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public class CustomerCaseData {

    @NotNull
    private String customerName;

    private String reference;

    private List<UUID> messageIds;

    public String getCustomerName() {
        return customerName;
    }

    public CustomerCaseData setCustomerName(String customerName) {
        this.customerName = customerName;
        return this;
    }

    public String getReference() {
        return reference;
    }

    public CustomerCaseData setReference(String reference) {
        this.reference = reference;
        return this;
    }

    public List<UUID> getMessageIds() {
        return messageIds;
    }
}
