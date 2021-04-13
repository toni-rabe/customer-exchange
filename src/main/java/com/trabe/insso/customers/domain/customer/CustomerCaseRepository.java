package com.trabe.insso.customers.domain.customer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerCaseRepository {

    Optional<CustomerCase> getCustomerCase(UUID customerCaseId);

    List<CustomerCase> getAll();

    CustomerCase save(CustomerCase customerCase);
}
