package com.trabe.insso.customers.infra.database;

import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

class CustomerCaseEntity {

    @NotNull
    UUID id;

    @NotNull
    String customerName;

    String reference;

    @NotNull
    OffsetDateTime creationDate;

    Set<UUID> messagesIds;
}
