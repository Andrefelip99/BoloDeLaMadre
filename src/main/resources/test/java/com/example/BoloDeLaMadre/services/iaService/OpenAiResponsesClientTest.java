package com.example.BoloDeLaMadre.services.iaService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

class OpenAiResponsesClientTest {

    @Test
    void sendsConversationAndNonStoredBusinessContextToResponsesApi() {
        RestClient.Builder builder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        OpenAiResponsesClient client = new OpenAiResponsesClient("test-secret", "gpt-4.1-mini", builder.build());
        server.expect(requestTo("https://api.openai.com/v1/responses"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("Authorization", "Bearer test-secret"))
                .andExpect(jsonPath("$.model").value("gpt-4.1-mini"))
                .andExpect(jsonPath("$.store").value(false))
                .andExpect(jsonPath("$.instructions").value(org.hamcrest.Matchers.containsString("Dados consultados")))
                .andExpect(jsonPath("$.input[0].content").value("Quanto vendemos?"))
                .andRespond(withSuccess("""
                        {"output":[{"type":"message","content":[{"type":"output_text","text":"Vendemos R$ 250."}]}]}
                        """, MediaType.APPLICATION_JSON));

        String answer = client.respond("Faturamento R$ 250", List.of(Map.of("role", "user", "content", "Quanto vendemos?")));

        assertThat(answer).isEqualTo("Vendemos R$ 250.");
        server.verify();
    }

    @Test
    void reportsMissingApiKeyClearly() {
        OpenAiResponsesClient client = new OpenAiResponsesClient(" ", "gpt-4.1-mini", RestClient.create());
        assertThatThrownBy(() -> client.respond("contexto", List.of()))
                .isInstanceOf(AssistantUnavailableException.class)
                .hasMessageContaining("OPENAI_API_KEY");
    }
}
