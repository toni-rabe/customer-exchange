package com.trabe.insso.customers.integration;

import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.ValueMatcher;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static com.trabe.insso.customers.integration.DocUtils.*;
import static java.time.ZoneId.systemDefault;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
class FullExchangeScenarioIntegrationTest {

    private static final String CUSTOMER_NAME = "Jérémie Durand";
    private static final String CUSTOMER_MESSAGE_CONTENT = "Bonjour, j’ai un problème avec mon nouveau téléphone";
    private static final String CUSTOMER_SERVICE_AGENT = "Sonia Valentin";
    private static final String CUSTOMER_SERVICE_MESSAGE_CONTENT = "Je suis Sonia, et je vais mettre tout en œuvre pour vous aider. " +
            "Quel est le modèle de votre téléphone ?";
    private static final String CUSTOMER_REFERENCE = "KA-18B6";
    private static final String CHANNEL = "MAIL";
    private static final String TODAY_PLACE_HOLDER = "${TODAY}";

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;
    private UuidValueMatcher uuidValueMatcher;
    private TodayValueMatcher todayValueMatcher;

    private UUID customerMessageId;
    private String customerMessageCreationDate;
    private UUID customerCaseId;
    private String customerCaseCreationDate;
    private UUID customerServiceMessageId;
    private String customerServiceMessageCreationDate;

    @BeforeEach
    void setup(RestDocumentationContextProvider restDocumentation) {
        uuidValueMatcher = new UuidValueMatcher();
        todayValueMatcher = new TodayValueMatcher();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    void givenFullExchangeScenario_shouldExecuteEachStepWithExpectedOutcome() throws Exception {
        postCustomerMessage();

        postCreateCustomerCase();

        postCustomerServiceMessage();

        patchAddMessageToCustomerCase();

        putCustomerCaseUpdateReference();

        getAllCustomerCases();
    }

    private void postCustomerMessage() throws Exception {
        String response = mockMvc.perform(post("/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.name())
                .content(createMessageInputBody(CUSTOMER_NAME, CUSTOMER_MESSAGE_CONTENT)))
                .andExpect(status().isCreated())
                .andDo(document("post-customer-message",
                        requestHeaders(contentTypeHeaderDescriptor),
                        requestFields(messageRequestDescriptor),
                        responseFields(messageResponseDescriptor
                        )))
                .andReturn().getResponse().getContentAsString();

        checkResponseBodyWithCustomComparator(expectedCreateMessageOutput(CUSTOMER_NAME, CUSTOMER_MESSAGE_CONTENT), response);

        customerMessageId = uuidValueMatcher.getActualValue();
        customerMessageCreationDate = todayValueMatcher.actualTodayValue;
    }

    private void postCreateCustomerCase() throws Exception {
        String response;
        response = mockMvc.perform(post("/customer-cases")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.name())
                .content(createCustomerCaseInputBody(customerMessageId)))
                .andExpect(status().isCreated())
                .andDo(document("post-customer-case",
                        requestHeaders(contentTypeHeaderDescriptor),
                        requestFields(customerCaseRequestDescriptor),
                        customerCaseResponseFieldsSnippet))
                .andReturn().getResponse().getContentAsString();

        checkResponseBodyWithCustomComparator(expectedCustomerCaseCreationOutputBody(), response);
        customerCaseId = uuidValueMatcher.getActualValue();
        customerCaseCreationDate = todayValueMatcher.actualTodayValue;
    }

    private void postCustomerServiceMessage() throws Exception {
        String response;
        response = mockMvc.perform(post("/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.name())
                .content(createMessageInputBody(CUSTOMER_SERVICE_AGENT, CUSTOMER_SERVICE_MESSAGE_CONTENT)))
                .andExpect(status().isCreated())
                .andDo(document("post-customer-service-message",
                        requestHeaders(contentTypeHeaderDescriptor),
                        requestFields(messageRequestDescriptor),
                        responseFields(messageResponseDescriptor
                        )))
                .andReturn().getResponse().getContentAsString();


        checkResponseBodyWithCustomComparator(expectedCreateMessageOutput(CUSTOMER_SERVICE_AGENT, CUSTOMER_SERVICE_MESSAGE_CONTENT), response);
        customerServiceMessageId = uuidValueMatcher.getActualValue();
        customerServiceMessageCreationDate = todayValueMatcher.actualTodayValue;
    }

    private void patchAddMessageToCustomerCase() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/customer-cases/{customerCaseId}/add-message", customerCaseId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(addMessageInputBody(customerServiceMessageId)))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedCustomerCaseWithAllMessagesOutputBody(null)))
                .andDo(document("patch-add-message",
                        requestHeaders(contentTypeHeaderDescriptor),
                        pathParameters(parameterWithName("customerCaseId").description("The Id of the customer case on which the message will be added")),
                        requestFields(fieldWithPath("messageId").type(JsonFieldType.STRING).description("Id of the message to be added (UUID)")),
                        customerCaseResponseFieldsSnippet));
    }

    private void putCustomerCaseUpdateReference() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.put("/customer-cases/{customerCaseId}", customerCaseId)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.name())
                .content(updateCustomerCaseInputBody()))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedCustomerCaseWithAllMessagesOutputBody(CUSTOMER_REFERENCE)))
                .andDo(document("put-customer-case-reference",
                        requestHeaders(contentTypeHeaderDescriptor),
                        pathParameters(parameterWithName("customerCaseId").description("The Id of the customer case to be updated")),
                        requestFields(customerCaseRequestDescriptor),
                        customerCaseResponseFieldsSnippet));
    }

    private void getAllCustomerCases() throws Exception {
        mockMvc.perform(get("/customer-cases"))
                .andExpect(status().isOk())
                .andExpect(content().json(allCustomerCases()))
                .andDo(document("get-all",
                        responseFields(
                                fieldWithPath("[]").description("An array of customer cases"))
                                .andWithPrefix("[].", customerCaseResponseDescriptor)
                                .andWithPrefix("[].messages[].", messageResponseDescriptor))
                )
        ;
    }

    private String createMessageInputBody(String author, String content) {
        //language=json
        return "{\n" +
                "  \"author\": \"" + author + "\",\n" +
                "  \"channel\": \"" + CHANNEL + "\",\n" +
                "  \"content\": \"" + content + "\"\n" +
                "}";
    }

    private String expectedCreateMessageOutput(String author, String content) {
        //language=json
        return "{\n" +
                "  \"id\": \"CheckedWithCustomComparator\",\n" +
                "  \"author\": \"" + author + "\",\n" +
                "  \"channel\": \"" + CHANNEL + "\",\n" +
                "  \"content\": \"" + content + "\",\n" +
                "  \"creationDate\": \"" + TODAY_PLACE_HOLDER + "\"\n" +
                "}";
    }

    private String createCustomerCaseInputBody(UUID messageId) {
        //language=json
        return "{\n" +
                "  \"customerName\": \"" + CUSTOMER_NAME + "\",\n" +
                "  \"messageIds\": [\"" + messageId + "\"]\n" +
                "}";
    }

    private String expectedCustomerCaseCreationOutputBody() {
        //language=json
        return "{\n" +
                "  \"id\": \"CheckedWithCustomComparator\",\n" +
                "  \"customerName\": \"" + CUSTOMER_NAME + "\",\n" +
                "  \"reference\": null,\n" +
                "  \"creationDate\": \"${TODAY}\",\n" +
                "  \"messages\": [\n" +
                "    {\n" +
                "      \"id\": \"" + customerMessageId + "\",\n" +
                "      \"author\": \"" + CUSTOMER_NAME + "\",\n" +
                "      \"channel\": \"" + CHANNEL + "\",\n" +
                "      \"content\": \"" + CUSTOMER_MESSAGE_CONTENT + "\",\n" +
                "      \"creationDate\": \"" + customerMessageCreationDate + "\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
    }

    private String expectedCustomerCaseWithAllMessagesOutputBody(String customerReference) {
        String customerReferenceValue = customerReference != null ? "\"" + customerReference + "\"" : null;
        //language=json
        return "{\n" +
                "  \"id\": \"" + customerCaseId + "\",\n " +
                "  \"customerName\": \"" + CUSTOMER_NAME + "\",\n" +
                "  \"reference\": " + customerReferenceValue + ",\n" +
                "  \"creationDate\": \"" + customerCaseCreationDate + "\",\n" +
                "  \"messages\": [\n" +
                "    {\n" +
                "      \"id\": \"" + customerMessageId + "\",\n" +
                "      \"author\": \"" + CUSTOMER_NAME + "\",\n" +
                "      \"channel\": \"" + CHANNEL + "\",\n" +
                "      \"content\": \"" + CUSTOMER_MESSAGE_CONTENT + "\",\n" +
                "      \"creationDate\": \"" + customerMessageCreationDate + "\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"" + customerServiceMessageId + "\",\n" +
                "      \"author\": \"" + CUSTOMER_SERVICE_AGENT + "\",\n" +
                "      \"channel\": \"" + CHANNEL + "\",\n" +
                "      \"content\": \"" + CUSTOMER_SERVICE_MESSAGE_CONTENT + "\",\n" +
                "      \"creationDate\": \"" + customerServiceMessageCreationDate + "\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
    }

    private String addMessageInputBody(UUID messageId) {
        //language=json
        return "{\n" +
                "  \"messageId\": \"" + messageId + "\"\n" +
                "}";
    }

    private String updateCustomerCaseInputBody() {
        //language=json
        return "{\n" +
                "  \"customerName\": \"" + CUSTOMER_NAME + "\",\n" +
                "  \"reference\": \"" + CUSTOMER_REFERENCE + "\",\n" +
                "  \"messageIds\": [\"" + customerMessageId + "\", \"" + customerServiceMessageId + "\"]\n" +
                "}";
    }

    private String allCustomerCases() {
        return "[" + expectedCustomerCaseWithAllMessagesOutputBody(CUSTOMER_REFERENCE) + "]";
    }

    private void checkResponseBodyWithCustomComparator(String expected, String actual) throws JSONException {
        final String today = OffsetDateTime.now(systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        expected = expected.replace(TODAY_PLACE_HOLDER, today);

        JSONAssert.assertEquals(expected, actual, customComparator());
    }

    private CustomComparator customComparator() {
        return new CustomComparator(JSONCompareMode.NON_EXTENSIBLE,
                new Customization("id", uuidValueMatcher),
                new Customization("creationDate", todayValueMatcher));
    }

    static class UuidValueMatcher implements ValueMatcher<Object> {


        private UUID actualValue;

        @Override
        public boolean equal(Object actual, Object expected) {
            try {
                actualValue = UUID.fromString(actual.toString());
            } catch (Exception e) {
                return false;
            }
            return true;
        }

        UUID getActualValue() {
            return actualValue;
        }

    }

    static class TodayValueMatcher implements ValueMatcher<Object> {

        private String actualTodayValue;

        @Override
        public boolean equal(Object actual, Object expected) {
            String actualString = actual.toString();
            actualTodayValue = actualString;
            String expectedString = expected.toString();

            return actualString.startsWith(expectedString);
        }
    }
}
