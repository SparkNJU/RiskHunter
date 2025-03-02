package com.RiskHunter.service;/*
 * @date 03/02 15:13
 */

import com.RiskHunter.po.ChatRecord;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.nio.channels.MembershipKey;
import java.time.Duration;
import java.util.*;

import org.springframework.beans.factory.annotation.Value;

@Slf4j
@Service
public class ModelService {
    private final WebClient webClient;
    private final StringBuilder fullResponse = new StringBuilder();
    private final ChatService chatService;
    //超时时间
    private static final Duration TIMEOUT = Duration.ofSeconds(300);

    public ModelService(@Value("${model.api-key}") String apiKey,
                        @Value("${model.base-url}") String baseUrl, ChatService chatService) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();
        this.chatService = chatService;
    }

   

    private Optional<String> parseChunk(String chunk) {
        try {
            JsonNode node = new ObjectMapper().readTree(chunk);
            return Optional.ofNullable(node.path("choices").get(0)
                    .path("delta").path("content").asText());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public String getFullResponse() {
        return fullResponse.toString();
    }

    public String completeChat(String message, Long sessionId, Long userId) {
        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "deepseek-r1");

            // Construct the message list
            List<Map<String, String>> messages = new ArrayList<>();

            //先通过getHistory获取历史消息
            List<ChatRecord> history = chatService.getHistory(sessionId, userId);
            //然后将历史消息和当前消息一起传入，符合下面的messageEntry格式
            for (ChatRecord record : history) {
                Map<String, String> messageEntry = new HashMap<>();
                messageEntry.put("role", record.getDirection() ? "user" : "assistant");
                messageEntry.put("content", record.getContent());
                messages.add(messageEntry);
            }
            Map<String, String> messageEntry = new HashMap<>();
            messageEntry.put("role", "user");
            messageEntry.put("content", message);
            messages.add(messageEntry);

            log.info("messages: {}", messages.toString());
            requestBody.put("messages", messages);
            requestBody.put("stream", false); // 关闭流式模式


            JsonNode response = webClient.post()
                    .uri("/chat/completions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block(TIMEOUT); // 设置超时时间

            if (response != null && response.has("choices") && response.get("choices").isArray() && response.get("choices").size() > 0) {
                JsonNode firstChoice = response.get("choices").get(0);
                if (firstChoice.has("message") && firstChoice.get("message").has("content")) {
                    return firstChoice.get("message").get("content").asText();
                } else {
                    throw new RuntimeException("大模型返回结果格式错误: 缺少 message 或 content 字段");
                }
            } else {
                throw new RuntimeException("大模型返回结果格式错误: 缺少 choices 字段或 choices 为空");
            }


        } catch (Exception e) {
            throw new RuntimeException("大模型调用失败: " + e.getMessage(), e); // Include the original exception
        }
    }
}