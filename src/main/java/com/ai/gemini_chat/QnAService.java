package com.ai.gemini_chat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class QnAService {

    @Value("${ai.api.url}")
    private String apiUrl;

    @Value("${ai.api.key}")
    private String apiKey;

    @Value("${ai.model}")
    private String model;


    private final WebClient webClient;
    private final ObjectMapper objectMapper;


    public QnAService(WebClient.Builder builder) {
        this.webClient = builder.build();
        this.objectMapper = new ObjectMapper();
    }


    public String getAnswer(String question) {


        Map<String, Object> requestBody = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of(
                                "role", "user",
                                "content", question
                        )
                ),
                "max_tokens", 1000
        );


        try {

            String response = webClient.post()
                    .uri(apiUrl)
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .onStatus(
                            status -> status.isError(),
                            clientResponse ->
                                    clientResponse.bodyToMono(String.class)
                                            .map(error ->
                                                    new RuntimeException(
                                                            "OpenRouter Error: "
                                                                    + error
                                                    )
                                            )
                    )
                    .bodyToMono(String.class)
                    .block();


            JsonNode json = objectMapper.readTree(response);


            if(json.has("error")) {
                return "API Error: " +
                        json.get("error")
                                .get("message")
                                .asText();
            }


            return json
                    .get("choices")
                    .get(0)
                    .get("message")
                    .get("content")
                    .asText();


        } catch(Exception e) {

            e.printStackTrace();

            return "Failed: " + e.getMessage();
        }
    }
}