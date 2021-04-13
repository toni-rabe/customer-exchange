package com.trabe.insso.customers.api.customer;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public class AddMessageDto {

    @NotNull
    private UUID messageId;

    public UUID getMessageId() {
        return messageId;
    }
}
