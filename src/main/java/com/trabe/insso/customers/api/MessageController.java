package com.trabe.insso.customers.api;

import com.trabe.insso.customers.domain.message.Message;
import com.trabe.insso.customers.domain.message.MessageData;
import com.trabe.insso.customers.domain.message.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping(value = "/messages", produces = "application/json;charset=utf-8")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    ResponseEntity<Message> createMessage(@Valid @RequestBody MessageData messageData) {
        @Valid final Message message = messageService.createMessageFrom(messageData);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(message);
    }

    @GetMapping("/{messageId}")
    ResponseEntity<Message> getMessage(@PathVariable("messageId") UUID messageId) {
        return ResponseEntity.of(messageService.getMessage(messageId));
    }

    @PutMapping("/{messageId}")
    ResponseEntity<Message> updateMessage(@PathVariable("messageId") UUID messageId, @RequestBody @Valid MessageData messageData) {
        return ResponseEntity.of(messageService.updateMessageFrom(messageId, messageData));
    }
}
