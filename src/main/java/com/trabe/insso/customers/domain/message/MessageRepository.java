package com.trabe.insso.customers.domain.message;

import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {

    Optional<Message> getMessage(UUID messageId);

    Message save(Message message);
}
