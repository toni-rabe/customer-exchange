package com.trabe.insso.customers.domain.customer;

import com.trabe.insso.customers.domain.message.Message;

import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.*;

public class CustomerCase {

    @NotNull
    private final UUID id;

    @NotNull
    private String customerName;

    private String reference;

    @NotNull
    private final OffsetDateTime creationDate;

    private List<Message> messages = new ArrayList<>();

    public CustomerCase(UUID id, OffsetDateTime creationDate) {
        this.id = id;
        this.creationDate = creationDate;
    }

    public CustomerCase setCustomerName(String customerName) {
        this.customerName = customerName;
        return this;
    }

    public CustomerCase setReference(String reference) {
        this.reference = reference;
        return this;
    }

    public CustomerCase addMessage(Message message) {
        this.messages.add(message);
        return this;
    }

    public CustomerCase setMessages(List<Message> messages) {
        this.messages = messages;
        return this;
    }

    public UUID getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getReference() {
        return reference;
    }

    public OffsetDateTime getCreationDate() {
        return creationDate;
    }

    public List<Message> getMessages() {
        return Collections.unmodifiableList(messages);
    }
}
