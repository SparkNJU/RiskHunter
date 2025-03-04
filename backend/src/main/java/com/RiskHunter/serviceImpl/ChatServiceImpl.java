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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

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
        log.info("user {} 创建新会话: {}", userId, session.getId());
        return session.getId();
    }

    @Override
    public String chatWithoutStream(Long sessionId, String message, Long userId) {
        // 检查三个参数都合法
        if (sessionId == null || userId == null || message == null) {
            throw new IllegalArgumentException("参数不完整");
        }
        log.info("Chat Without Stream");
        // 调用大模型API
        String fullResponse = "";
        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "deepseek-r1");

            List<Map<String, String>> messages = buildMessageHistory(sessionId, userId, message);
            log.info("API请求消息体: {}", messages);
            requestBody.put("messages", messages);
            requestBody.put("stream", false);
            Map<String, Boolean> parameters = new HashMap<>();
            parameters.put("has_thoughts", true);
            requestBody.put("parameters", parameters);

            JsonNode response = webClient.post()
                    .uri("/chat/completions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block(Duration.ofSeconds(300));
            fullResponse = parseResponse(response);

        } catch (Exception e) {
            log.error("大模型调用失败: {}", e.getMessage(), e);
            fullResponse = "当前服务繁忙，请稍后再试";
        }
        log.info("API响应: {}", fullResponse);
        // 保存用户消息
        saveChatRecord(sessionId, userId, message, true);
        // 保存AI响应
        saveChatRecord(sessionId, userId, fullResponse, false);

        return fullResponse;
    }

    @Override

    public Flux<String> chatWithStream(Long sessionId, String message, Long userId) {
        // cz 0304 18:44 version
        // 检查三个参数都合法
        if (sessionId == null || userId == null || message == null) {
            throw new IllegalArgumentException("参数不完整");
        }
        log.info("Chat With Stream");
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "deepseek-r1");
        List<Map<String, String>> messages = buildMessageHistory(sessionId, userId, message);
        log.info("API请求消息体: {}", messages);
        requestBody.put("messages", messages);
        requestBody.put("stream", true);

        // 添加stream_options参数
        Map<String, Object> streamOptions = new HashMap<>();
        streamOptions.put("include_usage", true);
        requestBody.put("stream_options", streamOptions);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("incremental_output", true);
        parameters.put("has_thoughts", true);
        requestBody.put("parameters", parameters);

        // 保存用户消息
        saveChatRecord(sessionId, userId, message, true);

        // 用于收集完整响应的StringBuilder
        StringBuilder fullResponse = new StringBuilder();

        return webClient.post()
                .uri("/chat/completions")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-DashScope-SSE", "enable")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToFlux(String.class) // 修改为接收String类型
                .filter(rawData -> {
                    // 过滤掉 "data: [DONE]" 这种特殊标记
                    return !rawData.trim().equals("[DONE]") && !rawData.trim().isEmpty();
                })
                .map(rawData -> {
                    try {
                        // 尝试解析为JSON
                        ObjectMapper mapper = new ObjectMapper();
                        JsonNode node = mapper.readTree(rawData);
                        return parseStreamResponse(node);
                    } catch (Exception e) {
                        log.warn("无法解析为JSON: {}, 错误: {}", rawData, e.getMessage());
                        return "";
                    }
                })
                .filter(chunk -> chunk != null && !chunk.isEmpty()) // 过滤掉空响应
                .doOnNext(chunk -> {
                    // 打印每次返回的流式消息片段 经过测试，这里的chunk是每次返回的消息片段 所以加上注释
                    //log.info("Received stream chunk: {}", chunk);
                    // 累积响应内容
                    fullResponse.append(chunk);
                })
                .doOnComplete(() -> {
                    // 流结束时保存完整的AI响应
                    saveChatRecord(sessionId, userId, fullResponse.toString(), false);
                    log.info("流式响应完成，已保存完整回复");
                })
                .doOnError(error -> {
                    log.error("流式调用失败: {}", error.getMessage(), error);
                    // 保存错误信息作为AI响应
                    saveChatRecord(sessionId, userId, "处理请求时发生错误，请稍后再试", false);
                })
                .onErrorResume(e -> {
                    // 出错时返回错误信息
                    return Flux.just("处理请求时发生错误，请稍后再试");
                })
                .timeout(Duration.ofSeconds(60)); // 添加超时处理
    }

    private String parseStreamResponse(JsonNode response) {
        if (response == null || response.isEmpty()) {
            return "";
        }

        // 如果是结束标志或usage统计信息
        if (!response.has("choices") || response.get("choices").isEmpty()) {
            return "";
        }

        JsonNode choices = response.get("choices");
        if (choices.size() == 0) {
            return "";
        }

        JsonNode delta = choices.get(0).get("delta");
        if (delta == null) {
            return "";
        }

        // 处理思考过程
        if (delta.has("reasoning_content") && !delta.get("reasoning_content").isNull()) {
            String thoughtContent = delta.get("reasoning_content").asText();
            if (!thoughtContent.isEmpty()) {
                return "[思考]" + thoughtContent;
            }
        }

        // 处理实际回答内容
        if (delta.has("content") && !delta.get("content").isNull()) {
            String content = delta.get("content").asText();
            if (content != null && !content.isEmpty()) {
                return content;
            }
        }

        return "";
    }

    private void saveChatRecord(Long sessionId, Long userId, String content, boolean isUser) {
        ChatRecord record = new ChatRecord();
        record.setSessionId(sessionId);
        record.setUserId(userId);
        record.setDirection(isUser);
        // 在保存之前清理思考标记
        record.setContent(cleanThoughtMarkers(content));
        record.setCreateTime(LocalDateTime.now());
        recordMapper.insert(record);
    }

    private String cleanThoughtMarkers(String content) {
        // 使用正则表达式移除所有[思考]标记
        return content.replaceAll("\\[思考\\]", "");
    }

    @Override
    public List<ChatRecord> getHistory(Long sessionId, Long userId) {
        return recordMapper.selectList(new QueryWrapper<ChatRecord>()
                .eq("session_id", sessionId)
                .eq("user_id", userId)
                .orderByAsc("create_time"));
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