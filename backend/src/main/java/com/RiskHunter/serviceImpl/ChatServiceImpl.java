package com.RiskHunter.serviceImpl;

import com.RiskHunter.Mapper.ChatRecordMapper;
import com.RiskHunter.Mapper.ChatSessionMapper;
import com.RiskHunter.po.ChatRecord;
import com.RiskHunter.po.ChatSession;
import com.RiskHunter.service.ChatService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatSessionMapper sessionMapper;
    private final ChatRecordMapper recordMapper;
    private WebClient webClient;

    @Value("${model.api-key}")
    private String apiKey;

    @Value("${model.base-url}")
    private String baseUrl;

    @PostConstruct
    public void init() {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();
    }

    @Override
    public Long createSession(Long userId) {
        ChatSession session = new ChatSession();
        session.setUserId(userId);
        session.setCreateTime(LocalDateTime.now());
        sessionMapper.insert(session);
        log.info("创建新会话: {}", session.getId());
        return session.getId();
    }

    @Override
    public String chat(Long sessionId, String message, Long userId) {
        // 调用大模型API
        String fullResponse = completeChat(message, sessionId, userId);
        // 保存用户消息
        saveChatRecord(sessionId, userId, message, true);
        // 保存AI响应
        saveChatRecord(sessionId, userId, fullResponse, false);

        return fullResponse;
    }

    private void saveChatRecord(Long sessionId, Long userId, String content, boolean isUser) {
        ChatRecord record = new ChatRecord();
        record.setSessionId(sessionId);
        record.setUserId(userId);
        record.setDirection(isUser);
        record.setContent(content);
        record.setCreateTime(LocalDateTime.now());
        recordMapper.insert(record);
    }

    @Override
    public List<ChatRecord> getHistory(Long sessionId, Long userId) {
        return recordMapper.selectList(new QueryWrapper<ChatRecord>()
                .eq("session_id", sessionId)
                .eq("user_id", userId)
                .orderByAsc("create_time"));
    }

    private String completeChat(String message, Long sessionId, Long userId) {
        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "deepseek-r1");

            List<Map<String, String>> messages = buildMessageHistory(sessionId, userId, message);

            log.info("API请求消息体: {}", messages);
            requestBody.put("messages", messages);
            requestBody.put("stream", false);

            JsonNode response = webClient.post()
                    .uri("/chat/completions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block(Duration.ofSeconds(300));

            return parseResponse(response);
        } catch (Exception e) {
            log.error("大模型调用失败: {}", e.getMessage(), e);
            return "当前服务繁忙，请稍后再试";
        }
    }

    private List<Map<String, String>> buildMessageHistory(Long sessionId, Long userId, String currentMessage) {
        List<Map<String, String>> messages = new ArrayList<>();
        for (ChatRecord record : getHistory(sessionId, userId)) {
            messages.add(createMessageEntry(record.getDirection() ? "user" : "assistant", record.getContent()));
        }
        messages.add(createMessageEntry("user", currentMessage));
        return messages;
    }

    private Map<String, String> createMessageEntry(String role, String content) {
        Map<String, String> entry = new HashMap<>();
        entry.put("role", role);
        entry.put("content", content);
        return entry;
    }

    private String parseResponse(JsonNode response) {
        if (response == null || !response.has("choices")) {
            throw new RuntimeException("无效的API响应格式");
        }

        JsonNode choices = response.get("choices");
        if (!choices.isArray() || choices.size() == 0) {
            throw new RuntimeException("空的API响应结果");
        }

        JsonNode messageNode = choices.get(0).path("message");
        if (messageNode.isMissingNode() || !messageNode.has("content")) {
            throw new RuntimeException("响应中缺少消息内容");
        }

        return messageNode.get("content").asText();
    }
}