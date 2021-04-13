package com.trabe.insso.customers.infra.database;

import com.trabe.insso.customers.domain.customer.CustomerCase;
import com.trabe.insso.customers.domain.message.Message;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
class CustomerCaseMapper {

    CustomerCaseEntity createEntityFrom(CustomerCase customerCase) {
        CustomerCaseEntity customerCaseEntity = new CustomerCaseEntity();
        customerCaseEntity.id = customerCase.getId();
        customerCaseEntity.customerName = customerCase.getCustomerName();
        customerCaseEntity.reference = customerCase.getReference();
        customerCaseEntity.creationDate = customerCase.getCreationDate();
        customerCaseEntity.messagesIds = customerCase.getMessages().stream()
                .map(Message::getId)
                .collect(Collectors.toSet());
        return customerCaseEntity;
    }
}
