package com.trabe.insso.customers.infra.database;

import com.trabe.insso.customers.domain.customer.CustomerCase;
import com.trabe.insso.customers.domain.customer.CustomerCaseRepository;
import com.trabe.insso.customers.domain.message.Message;
import com.trabe.insso.customers.domain.message.MessageNotFoundException;
import com.trabe.insso.customers.domain.message.MessageRepository;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryCustomerCaseRepository implements CustomerCaseRepository {

    private final CustomerCaseMapper customerCaseMapper;

    private final MessageRepository messageRepository;

    private Map<UUID, CustomerCaseEntity> customerCases = new HashMap<>();

    public InMemoryCustomerCaseRepository(CustomerCaseMapper customerCaseMapper, MessageRepository messageRepository) {
        this.customerCaseMapper = customerCaseMapper;
        this.messageRepository = messageRepository;
    }

    @Override
    public Optional<CustomerCase> getCustomerCase(UUID customerCaseId) {
        return Optional.ofNullable(customerCases.get(customerCaseId))
                .map(this::convertCustomerCaseEntity);
    }

    @Override
    public List<CustomerCase> getAll() {
        return customerCases.values().stream()
                .map(this::convertCustomerCaseEntity)
                .collect(Collectors.toList());
    }

    private CustomerCase convertCustomerCaseEntity(CustomerCaseEntity customerCaseEntity) {
        return new CustomerCase(customerCaseEntity.id, customerCaseEntity.creationDate)
                .setCustomerName(customerCaseEntity.customerName)
                .setReference(customerCaseEntity.reference)
                .setMessages(customerCaseEntity.messagesIds.stream()
                        .map(this::retrieveMessage)
                        .collect(Collectors.toList()));
    }

    private Message retrieveMessage(UUID messageId) {
        return messageRepository.getMessage(messageId)
                .orElseThrow(() -> new MessageNotFoundException(messageId));
    }

    @Override
    public CustomerCase save(CustomerCase customerCase) {
        final UUID customerCaseId = customerCase.getId();
        Objects.requireNonNull(customerCaseId, "CustomerCase Id cannot be null");
        customerCases.put(customerCaseId, customerCaseMapper.createEntityFrom(customerCase));
        return customerCase;
    }
}
