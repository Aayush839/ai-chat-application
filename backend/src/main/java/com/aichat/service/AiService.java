package com.aichat.service;

import com.aichat.model.Message;
import com.aichat.repository.MessageRepository;
import com.aichat.utility.UtilityClasses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.*;

@Service
public class AiService {

    @Value("${gemini.api.key}") // Recommended: Rename from openai.api.key to avoid confusion
    private String apiKey;

    @Autowired
    private RateLimiterService rateLimiterService;

    @Autowired
    public MessageRepository messageRepository;

    private String sanitizeInput(String input) {
        if (input == null) return "";

        return input
                .replaceAll("<", "")
                .replaceAll(">", "")
                .replaceAll("\\{", "")
                .replaceAll("}", "")
                .trim();
    }
    private boolean isAbusive(String input) {
        String lower = input.toLowerCase();

        return lower.contains("hate") ||
                lower.contains("kill") ||
                lower.contains("abuse");
    }
    public String getAIResponse(String userMessage, String conversationid) {

        String lower = userMessage.toLowerCase();

        if (lower.contains("ignore previous instructions") ||
                lower.contains("system prompt") ||
                lower.contains("act as") ||
                lower.contains("jailbreak") ||
                lower.contains("developer mode")) {

            return "Sorry, I can't process that request.";
        }

        if(isAbusive(userMessage))  return "Please use respectful language.";

        Long convId = UtilityClasses.getLong(conversationid);
        if (!rateLimiterService.isAllowed(convId)) {
            return "Too many requests. Please wait a moment and try again.";
        }

        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of(
                "role", "system",
                "content", "You are a professional customer support assistant. Reply in 2-3 lines."
        ));
        List<Message> previousMessages = messageRepository.findTop10ByConversationIdOrderByTimestampAsc(convId);
        if (previousMessages.size() > 10) {
            previousMessages = previousMessages.subList(
                    previousMessages.size() - 10,
                    previousMessages.size()
            );
        }
        for (Message msg : previousMessages) {
            messages.add(Map.of(
                    "role", msg.getType().equals("AI") ? "assistant" : "user",
                    "content", msg.getContent()
            ));
        }
        messages.add(Map.of(
                "role", "user",
                "content", userMessage
        ));


        String url = "https://openrouter.ai/api/v1/chat/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("HTTP-Referer", "http://localhost:3000");
        headers.set("X-Title", "AI Chat App");
        headers.setBearerAuth(apiKey);

//        body.put("model", "minimax/minimax-m2.5:free");
        Map<String, Object> body = new HashMap<>();
//        body.put("model", "nvidia/nemotron-3-super-120b-a12b:free");
        body.put("model", "openai/gpt-3.5-turbo");
        body.put("messages", messages);
        body.put("temperature", 0.7);
        body.put("max_tokens", 150);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();

        int maxRetries = 3;
        int attempt = 0;

        while (attempt < maxRetries) {
            try {
                ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
                Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                if (message == null || message.get("content") == null) {
                    return "Sorry, I couldn't generate a response.";
                }
                return message.get("content").toString();
            } catch (HttpClientErrorException e) {
                System.err.println("Error: " + e.getResponseBodyAsString());
                if (e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
                    attempt++;
                } else {
                    break;
                }
            }catch (Exception e) {
                System.err.println("Error in AI Service: " + e.getMessage());
                attempt++;
            }
            try {
                long delay = (long) Math.pow(2, attempt) * 500; // 500ms, 1s, 2s
                Thread.sleep(delay);
            } catch (InterruptedException ignored) {}
        }
        return "AI service unavailable. Please try again later.";
    }

}