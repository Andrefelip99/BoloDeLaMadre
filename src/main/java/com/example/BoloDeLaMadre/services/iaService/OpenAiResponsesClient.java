package com.example.BoloDeLaMadre.services.iaService;

import java.util.List;
import java.util.Map;
import java.net.http.HttpClient;
import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import tools.jackson.databind.JsonNode;

@Component
public class OpenAiResponsesClient {
    private static final Logger log = LoggerFactory.getLogger(OpenAiResponsesClient.class);
    private static final String RESPONSES_ENDPOINT = "https://api.openai.com/v1/responses";
    private static final String UNAVAILABLE_MESSAGE = "O assistente de IA está indisponível no momento.";

    private final String apiKey;
    private final String model;
    private final RestClient restClient;

    @Autowired
    public OpenAiResponsesClient(
            @Value("${openai.api-key:}") String apiKey,
            @Value("${openai.model:gpt-4.1-mini}") String model) {
        this(apiKey, model, createRestClient());
    }

    OpenAiResponsesClient(String apiKey, String model, RestClient restClient) {
        this.apiKey = apiKey;
        this.model = model;
        this.restClient = restClient;
    }

    public String respond(String businessContext, List<Map<String, String>> conversation) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new AssistantUnavailableException(
                    "O assistente ainda não foi configurado. Defina a variável OPENAI_API_KEY.");
        }

        String instructions = "Você é o assistente de gestão da confeitaria BoloDeLaMadre. "
                + "Responda em português brasileiro, com clareza e de forma prática. "
                + "Use os dados fornecidos como fonte factual; não invente números, produtos, vendas, estoque ou receitas. "
                + "Se os dados não forem suficientes, diga isso e faça uma pergunta objetiva. "
                + "Para estimativas de produção, respeite as unidades cadastradas e informe quando não puder convertê-las. "
                + "Não solicite nem revele dados pessoais de clientes. Não execute nem prometa alterações no sistema.\n\n"
                + "Dados consultados no sistema para esta pergunta:\n" + businessContext;

        Map<String, Object> request = Map.of(
                "model", model,
                "instructions", instructions,
                "input", conversation,
                "store", false,
                "max_output_tokens", 800);

        try {
            JsonNode response = restClient.post()
                    .uri(RESPONSES_ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .headers(headers -> headers.setBearerAuth(apiKey))
                    .body(request)
                    .retrieve()
                    .body(JsonNode.class);

            String text = extractOutputText(response);
            if (text == null || text.isBlank()) {
                String responseStatus = response == null ? "sem resposta" : response.path("status").asText("desconhecido");
                String incompleteReason = response == null ? "" : response.path("incomplete_details").path("reason").asText("");
                log.warn("OpenAI Responses API retornou sem texto. status={}, incompleteReason={}",
                        responseStatus, incompleteReason);
                throw new AssistantUnavailableException(UNAVAILABLE_MESSAGE);
            }
            return text.strip();
        } catch (AssistantUnavailableException ex) {
            throw ex;
        } catch (RestClientResponseException ex) {
            log.warn("OpenAI Responses API respondeu HTTP {} ({})",
                    ex.getStatusCode().value(), ex.getStatusText());
            throw new AssistantUnavailableException(UNAVAILABLE_MESSAGE, ex);
        } catch (RestClientException ex) {
            log.warn("Falha de comunicação com OpenAI Responses API ({})",
                    ex.getClass().getSimpleName());
            throw new AssistantUnavailableException(UNAVAILABLE_MESSAGE, ex);
        }
    }

    @SuppressWarnings("deprecation")
    private String extractOutputText(JsonNode response) {
        if (response == null || !response.has("output")) {
            return null;
        }
        StringBuilder text = new StringBuilder();
        for (JsonNode outputItem : response.path("output")) {
            for (JsonNode content : outputItem.path("content")) {
                if ("output_text".equals(content.path("type").asText())) {
                    if (!text.isEmpty()) {
                        text.append('\n');
                    }
                    text.append(content.path("text").asText());
                }
            }
        }
        return text.toString();
    }

    private static RestClient createRestClient() {
        HttpClient httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();
        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(httpClient);
        requestFactory.setReadTimeout(Duration.ofSeconds(45));
        return RestClient.builder().requestFactory(requestFactory).build();
    }
}
