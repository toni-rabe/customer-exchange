package com.trabe.insso.customers.api.customer;

import com.trabe.insso.customers.domain.customer.CustomerCase;
import com.trabe.insso.customers.domain.customer.CustomerCaseData;
import com.trabe.insso.customers.domain.customer.CustomerCaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/customer-cases", produces = "application/json;charset=utf-8")
public class CustomerCaseController {

    private final CustomerCaseService customerCaseService;

    public CustomerCaseController(CustomerCaseService customerCaseService) {
        this.customerCaseService = customerCaseService;
    }

    @PostMapping
    ResponseEntity<CustomerCase> createCustomerCase(@Valid @RequestBody CustomerCaseData customerCaseData) {
        return ResponseEntity.
                status(HttpStatus.CREATED)
                .body(customerCaseService.createFrom(customerCaseData));
    }

    @GetMapping("/{customerCaseId}")
    ResponseEntity<CustomerCase> getCustomerCase(@PathVariable("customerCaseId") UUID customerCaseId) {
        return ResponseEntity.of(customerCaseService.getCustomerCase(customerCaseId));
    }

    @GetMapping
    ResponseEntity<List<CustomerCase>> getAllCustomerCases() {
        return ResponseEntity.ok(customerCaseService.getAllCustomerCases());
    }

    @PutMapping("/{customerCaseId}")
    ResponseEntity<CustomerCase> updateCustomerCase(@PathVariable("customerCaseId") UUID customerCaseId,
                                                    @RequestBody @Valid CustomerCaseData customerCaseData) {
        return ResponseEntity.ok(customerCaseService.updateCustomerCaseFrom(customerCaseId, customerCaseData));
    }

    @PatchMapping("/{customerCaseId}/add-message")
    ResponseEntity<CustomerCase> addMessageTo(@PathVariable("customerCaseId") UUID customerCaseId,
                                              @RequestBody @Valid AddMessageDto addMessageDto) {
        return ResponseEntity.ok(customerCaseService.addMessageTo(customerCaseId, addMessageDto.getMessageId()));
    }
}
