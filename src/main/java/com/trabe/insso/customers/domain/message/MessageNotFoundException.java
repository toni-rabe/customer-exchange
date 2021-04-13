package com.trabe.insso.customers.domain.message;

import java.util.UUID;

public class MessageNotFoundException extends RuntimeException {

    public MessageNotFoundException(UUID messageId) {
        super(String.format("Message: %s not found", messageId));
    }
}
