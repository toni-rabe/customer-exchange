package com.trabe.insso.customers.domain.message;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.UUID;

public class Message {

    @NotNull
    private final UUID id;

    @NotBlank
    private String author;

    @NotNull
    private Channel channel;

    @NotNull
    private final OffsetDateTime creationDate;

    private String content;

    Message() {
        id = UUID.randomUUID();
        creationDate = OffsetDateTime.now();
    }

    public Message setAuthor(String author) {
        this.author = author;
        return this;
    }

    public Message setChannel(Channel channel) {
        this.channel = channel;
        return this;
    }

    public Message setContent(String content) {
        this.content = content;
        return this;
    }

    public UUID getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public Channel getChannel() {
        return channel;
    }

    public OffsetDateTime getCreationDate() {
        return creationDate;
    }

    public String getContent() {
        return content;
    }
}
