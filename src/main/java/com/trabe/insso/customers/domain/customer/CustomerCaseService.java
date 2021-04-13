package com.trabe.insso.customers.domain.customer;

import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Validated
public interface CustomerCaseService {

    @Valid CustomerCase createFrom(@Valid CustomerCaseData customerCaseData);

    Optional<CustomerCase> getCustomerCase(@NotNull UUID customerCaseId);

    List<CustomerCase> getAllCustomerCases();

    @Valid CustomerCase updateCustomerCaseFrom(@NotNull UUID customerCaseId, @Valid CustomerCaseData customerCaseData);

    @Valid CustomerCase addMessageTo(@NotNull UUID customerCaseId, @NotNull UUID messageId);
}
