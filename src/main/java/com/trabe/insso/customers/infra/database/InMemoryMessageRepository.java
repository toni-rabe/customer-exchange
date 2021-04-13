package com.trabe.insso.customers.infra.database;

import com.trabe.insso.customers.domain.message.Message;
import com.trabe.insso.customers.domain.message.MessageRepository;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class InMemoryMessageRepository implements MessageRepository {

    private Map<UUID, Message> messages = new HashMap<>();

    @Override
    public Optional<Message> getMessage(UUID messageId) {
        return Optional.ofNullable(messages.get(messageId));
    }

    @Override
    public Message save(Message message) {
        final UUID messageId = message.getId();
        Objects.requireNonNull(messageId, "MessageId cannot be null");
        messages.put(messageId, message);
        return message;
    }
}
