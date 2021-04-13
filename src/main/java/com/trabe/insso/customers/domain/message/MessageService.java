package com.trabe.insso.customers.domain.message;

import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;

@Validated
public interface MessageService {

    @Valid Message createMessageFrom(@Valid MessageData messageData);

    @Valid Optional<Message> getMessage(@NotNull UUID messageId);

    @Valid Optional<Message> updateMessageFrom(UUID messageId, MessageData messageData);
}
