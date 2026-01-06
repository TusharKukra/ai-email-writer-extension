package com.tkproject.email_writer.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tkproject.email_writer.entity.EmailDTO;
import com.tkproject.email_writer.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;

@Service
public class EmailServiceImpl implements EmailService {

    private final WebClient webclient;

    private final String apiKey;

    public EmailServiceImpl(WebClient.Builder webClientBuilder,
                            @Value("${gemini.api.url}") String baseUrl,
                            @Value("${gemini.api.key}") String geminiApiKey) {
        this.webclient = webClientBuilder.baseUrl(baseUrl).build();
        this.apiKey = geminiApiKey;
    }

    /**
     * @param emailDto
     * @return
     */
    @Override
    public String generateEmailReply(EmailDTO emailDto) {

        // Step 1 : Building prompt
        String prompt = buildRequestPrompt(emailDto);

        // Step 2 : Prepare RAW Json body (according to google gemini)
        String requestBody = String.format("""
                    {
                    "contents": [
                    {
                        "parts": [
                        {
                            "text":"%s"
                        }
                      ]
                    }
                  ]
                }""", prompt);

        // Step 3 : Send Request
        String response = webclient.post()
                .uri(uriBuilder -> uriBuilder.path("/v1beta/models/gemini-2.5-flash:generateContent")
                        .build())
                .header("x-goog-api-key", apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        // Step 4 : Extract response
        return extractResponseContent(response);
    }

    private String extractResponseContent(String response) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode node = objectMapper.readTree(response);
            return node.path("candidates").get(0)
                    .path("content")
                    .path("parts").get(0)
                    .path("text")
                    .asText();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method to create prompt
     *
     * @param emailDto
     * @return
     */
    private String buildRequestPrompt(EmailDTO emailDto) {
        StringBuilder promptBuilder = new StringBuilder();
        promptBuilder.append("Generate a professional email reply for the following email : ");
        if (emailDto.getEmailTone() != null && !emailDto.getEmailTone().isEmpty()) {
            promptBuilder.append("Use a ")
                    .append(emailDto.getEmailTone())
                    .append("tone.");
        }
        promptBuilder.append("Original email : \n")
                .append(emailDto.getEmailContent());
        return promptBuilder.toString();
    }
}
