package com.trabe.insso.customers.domain.customer;

import com.trabe.insso.customers.domain.message.Message;
import com.trabe.insso.customers.domain.message.MessageNotFoundException;
import com.trabe.insso.customers.domain.message.MessageService;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CustomerCaseServiceImpl implements CustomerCaseService {

    private final MessageService messageService;

    private final CustomerCaseRepository customerCaseRepository;

    public CustomerCaseServiceImpl(MessageService messageService, CustomerCaseRepository customerCaseRepository) {
        this.messageService = messageService;
        this.customerCaseRepository = customerCaseRepository;
    }

    @Override
    public CustomerCase createFrom(CustomerCaseData customerCaseData) {
        UUID id = UUID.randomUUID();
        OffsetDateTime creationDate = OffsetDateTime.now();
        final CustomerCase customerCase = new CustomerCase(id, creationDate)
                .setCustomerName(customerCaseData.getCustomerName())
                .setReference(customerCaseData.getReference())
                .setMessages(retrieveMessagesFrom(customerCaseData));

        return customerCaseRepository.save(customerCase);
    }

    @Override
    public Optional<CustomerCase> getCustomerCase(UUID customerCaseId) {
        return customerCaseRepository.getCustomerCase(customerCaseId);
    }

    @Override
    public List<CustomerCase> getAllCustomerCases() {
        return customerCaseRepository.getAll();
    }

    @Override
    public CustomerCase updateCustomerCaseFrom(UUID customerCaseId, CustomerCaseData customerCaseData) {
        CustomerCase customerCase = retrieveCustomerCase(customerCaseId);

        customerCase.setCustomerName(customerCaseData.getCustomerName())
                .setReference(customerCaseData.getReference())
                .setMessages(retrieveMessagesFrom(customerCaseData));

        return customerCaseRepository.save(customerCase);
    }

    @Override
    public CustomerCase addMessageTo(@NotNull UUID customerCaseId, @NotNull UUID messageId) {
        CustomerCase customerCase = retrieveCustomerCase(customerCaseId);
        customerCase.addMessage(retrieveMessage(messageId));
        return customerCaseRepository.save(customerCase);
    }

    private CustomerCase retrieveCustomerCase(UUID customerCaseId) {
        return customerCaseRepository.getCustomerCase(customerCaseId)
                .orElseThrow(() -> new CustomerCaseNotFoundException(customerCaseId));
    }

    private List<Message> retrieveMessagesFrom(CustomerCaseData customerCaseData) {
        if (customerCaseData.getMessageIds() != null) {
            return customerCaseData.getMessageIds().stream()
                    .map(this::retrieveMessage)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private Message retrieveMessage(UUID messageId) {
        return messageService.getMessage(messageId).orElseThrow(() -> new MessageNotFoundException(messageId));
    }
}
