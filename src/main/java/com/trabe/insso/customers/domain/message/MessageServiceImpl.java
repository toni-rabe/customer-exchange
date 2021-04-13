package com.trabe.insso.customers.domain.message;

import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Message createMessageFrom(MessageData messageData) {
        final Message message = new Message()
                .setAuthor(messageData.getAuthor())
                .setChannel(messageData.getChannel())
                .setContent(messageData.getContent());

        return messageRepository.save(message);
    }

    @Override
    public Optional<Message> getMessage(UUID messageId) {
        return messageRepository.getMessage(messageId);
    }

    @Override
    public Optional<Message> updateMessageFrom(UUID messageId, MessageData messageData) {
        return messageRepository.getMessage(messageId)
                .map((message ->
                        message.setAuthor(messageData.getAuthor())
                                .setContent(messageData.getContent())
                                .setChannel(messageData.getChannel())));
    }
}
