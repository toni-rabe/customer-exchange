package com.trabe.insso.customers.domain.message;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class MessageData {

    @NotEmpty
    private String author;

    @NotNull
    private Channel channel;

    private String content;

    public String getAuthor() {
        return author;
    }

    public Channel getChannel() {
        return channel;
    }

    public String getContent() {
        return content;
    }
}
