package com.trabe.insso.customers.integration;

import org.springframework.restdocs.headers.HeaderDescriptor;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

class DocUtils {

    static final FieldDescriptor[] messageResponseDescriptor = {
            fieldWithPath("id").type(JsonFieldType.STRING).description("Message generated identifier. UUID"),
            fieldWithPath("author").type(JsonFieldType.STRING).description("Author of the message"),
            fieldWithPath("channel").type(JsonFieldType.STRING)
                    .description("Message channel, possible values: [MAIL, SMS, FACEBOOK, TWITTER]"),
            fieldWithPath("creationDate").type(JsonFieldType.STRING).description("Date of the message"),
            fieldWithPath("content").type(JsonFieldType.STRING).description("Content of the message")
    };

    static final FieldDescriptor[] messageRequestDescriptor = {
            fieldWithPath("author").type(JsonFieldType.STRING).description("Author of the message"),
            fieldWithPath("channel").type(JsonFieldType.STRING)
                    .description("Message channel, possible values: [MAIL, SMS, FACEBOOK, TWITTER]"),
            fieldWithPath("content").type(JsonFieldType.STRING).description("Content of the message")
    };

    static final FieldDescriptor[] customerCaseResponseDescriptor = {
            fieldWithPath("id").type(JsonFieldType.STRING).description("Customer case generated identifier. UUID"),
            fieldWithPath("customerName").type(JsonFieldType.STRING).description("Customer name"),
            fieldWithPath("reference").type(JsonFieldType.STRING).description("Customer reference").optional(),
            fieldWithPath("creationDate").type(JsonFieldType.STRING).description("Date of customer case creation"),
            subsectionWithPath("messages").type(JsonFieldType.ARRAY).description("An array of Messages")
    };

    static final FieldDescriptor[] customerCaseRequestDescriptor = {
            fieldWithPath("customerName").type(JsonFieldType.STRING).description("Customer name"),
            fieldWithPath("reference").type(JsonFieldType.STRING).description("Customer reference").optional(),
            subsectionWithPath("messageIds").type(JsonFieldType.ARRAY).description("An array of MessagesIds (UUID)")
    };

    static final HeaderDescriptor[] contentTypeHeaderDescriptor = {
            headerWithName("Content-Type").description("application/json")
    };

    static final ResponseFieldsSnippet customerCaseResponseFieldsSnippet = responseFields(customerCaseResponseDescriptor)
            .andWithPrefix("messages[].", messageResponseDescriptor);

}
