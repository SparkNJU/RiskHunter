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
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatSessionMapper sessionMapper;
    private final ChatRecordMapper recordMapper;

    @Value("${model.api-key}")
    private String apiKey;

    @Value("${model.deepseek.base-url}")
    private String deepseekBaseUrl;

    @Value("${model.dashscope.base-url}")
    private String dashscopeBaseUrl;

    @Value("${model.dashscope.app-id}")
    private String appId;

    // 创建两个WebClient实例
    private WebClient deepseekClient;
    private WebClient dashscopeClient;

    @PostConstruct
    public void init() {
        // 普通对话客户端
        this.deepseekClient = WebClient.builder()
                .baseUrl(deepseekBaseUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();

        // RAG对话客户端
        this.dashscopeClient = WebClient.builder()
                .baseUrl(dashscopeBaseUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();
    }

    @Override
    public Long createSession(Long userId) {
        ChatSession session = new ChatSession();
        session.setTitle("");
        session.setUserId(userId);
        session.setCreateTime(LocalDateTime.now());
        session.setUpdateTime(LocalDateTime.now());
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

            JsonNode response = deepseekClient.post()
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
    public Flux<String> chatWithStream(Long sessionId, String message, Long userId,String modelName) {
        // cz 0304 18:44 version
        // 检查三个参数都合法
        if (sessionId == null || userId == null || message == null) {
            throw new IllegalArgumentException("参数不完整");
        }
        log.info("Chat With Stream");
        Map<String, Object> requestBody = new HashMap<>();
        if (modelName == null || modelName.isEmpty()) {
            modelName = "deepseek-r1";
        }
        requestBody.put("model", modelName);
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

        return deepseekClient.post()
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
                    // log.info("Received stream chunk: {}", chunk);
                    // 累积响应内容
                    fullResponse.append(chunk);
                })
                .doOnComplete(() -> {
                    // 流结束时保存完整的AI响应
                    saveChatRecord(sessionId, userId, fullResponse.toString(), false);
                    log.info("流式响应完成，已保存完整回复");
                })
                .doOnError(e ->
                {
                    log.warn("流式对话异常: {}", e.getMessage());
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
                return "<thought>" + thoughtContent + "</thought>";
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
        content = keepFirstAndLastThought(content);
        log.info("保存消息: {}", content);
        record.setContent(content);
        record.setCreateTime(LocalDateTime.now());
        recordMapper.insert(record);
    }

    public static String keepFirstAndLastThought(String input) {
        int firstThoughtStart = input.indexOf("<thought>");
        int lastThoughtEnd = input.lastIndexOf("</thought>");

        if (firstThoughtStart == -1 || lastThoughtEnd == -1 || firstThoughtStart >= lastThoughtEnd) {
            return input; // 如果没有找到完整的thought标签对，返回原字符串
        }
        // 提取第一个<thought>之后的内容
        String beforeFirstThought = input.substring(0, firstThoughtStart);
        // 提取最后一个</thought>之后的内容
        String afterLastThought = "";
        if (lastThoughtEnd + 10 < input.length()) {
            afterLastThought = input.substring(lastThoughtEnd + 10);
        }
        // 将所有thought标签内的内容连接起来
        String contentWithinThoughts = input.substring(firstThoughtStart + 9, lastThoughtEnd);
        // 移除中间所有的<thought>和</thought>标签
        contentWithinThoughts = contentWithinThoughts.replaceAll("</?thought>", "");
        // 构建最终结果
        return beforeFirstThought + "<thought>" + contentWithinThoughts + "</thought>" + afterLastThought;
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
        messages.add(createMessageEntry("user", "现在你是RiskHunter AI，Risk Hunter 是一个前沿的统计与AI工具，能够整合分析过往趋势，通过多维度的数据模型精准捕捉汇率风险信号，为企业提供更具前瞻性、科学性的风险管理支持。"));
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

    @Override
    public Boolean updateSessionTitle(Long sessionId, Long userId, String title) {
        // 使用QueryWrapper查询指定sessionId和userId的会话
        ChatSession session = sessionMapper.selectOne(
                new QueryWrapper<ChatSession>()
                        .eq("id", sessionId)
                        .eq("user_id", userId));

        if (session != null) {
            // 设置新标题并更新
            session.setTitle(title);
            session.setUpdateTime(LocalDateTime.now());
            int updated = sessionMapper.updateById(session);
            return updated > 0;
        }
        return false;
    }


    private void updateSessionTitleIfEmpty(Long sessionId, Long userId, String message) {
        // 使用QueryWrapper查询指定sessionId和userId的会话
        ChatSession session = sessionMapper.selectOne(
                new QueryWrapper<ChatSession>()
                        .eq("id", sessionId)
                        .eq("user_id", userId));

        if (session != null && (session.getTitle() == null || session.getTitle().isEmpty())) {
            // 从消息中提取前几个字作为标题
            String title = extractTitleFromMessage(message);
            session.setTitle(title);
            session.setUpdateTime(LocalDateTime.now());
            sessionMapper.updateById(session);
        }
    }

    private String extractTitleFromMessage(String message) {
        // 从消息中提取前10个字符作为标题，避免过长
        int titleLength = Math.min(10, message.length());
        String title = message.substring(0, titleLength);

        // 如果截断了单词，可以添加省略号
        if (message.length() > titleLength) {
            title += "...";
        }

        return title;
    }

    @Override
    public List<ChatSession> getSessionsByUserId(Long userId) {
        // 使用QueryWrapper查询指定userId的所有会话，按更新时间降序排列
        List<ChatSession> sessions = sessionMapper.selectList(
                new QueryWrapper<ChatSession>()
                        .eq("user_id", userId)
                        .orderByDesc("update_time") // 修改为按更新时间降序排列
        );
        return sessions.stream()
                .collect(Collectors.toList());
    }


    @Override
    public Flux<String> ragChatWithStream(Long sessionId, String message, Long userId) {
        if (sessionId == null || userId == null || message == null) {
            return Flux.error(new IllegalArgumentException("参数不完整"));
        }

        // 构建符合阿里云API规范的请求体
        Map<String, Object> requestBody = new HashMap<>();

        // 输入参数结构
        Map<String, Object> input = new HashMap<>();
        input.put("prompt", message);
        requestBody.put("input", input);

        // 参数配置
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("incremental_output", true); // 增量输出
        parameters.put("has_thoughts", true);       // 显示思考过程

        // RAG配置（根据实际知识库ID修改）
        //Map<String, Object> ragOptions = new HashMap<>();
        //ragOptions.put("pipeline_ids", Collections.singletonList("your_pipeline_id"));
        //parameters.put("rag_options", ragOptions);

        requestBody.put("parameters", parameters);

        // 调试参数（可选）
        requestBody.put("debug", Collections.emptyMap());

        // 保存用户消息
        saveChatRecord(sessionId, userId, message, true);

        StringBuilder fullResponse = new StringBuilder();

        return dashscopeClient.post()
                .uri("/api/v1/apps/{appId}/completion", "fe21741cec104e6a83226e989de14eb6")
                .header("X-DashScope-SSE", "enable")
                .header("Content-Type", "application/json")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToFlux(String.class)
                .filter(rawData -> {
                    // 过滤结束标记和空数据
                    return !rawData.trim().equals("[DONE]") && !rawData.trim().isEmpty();
                })
                .map(rawData -> {
                    try {
                        // 处理SSE格式前缀并解析
                        String jsonStr = rawData.replaceFirst("^data: ", "");
                        JsonNode node = new ObjectMapper().readTree(jsonStr);

                        // 解析响应内容
                        if (node.has("output") && node.get("output").has("text")) {
                            return node.get("output").get("text").asText();
                        }
                        // 处理知识库引用
                        if (node.has("doc_references")) {
                            return formatDocReferences(node.get("doc_references"));
                        }
                        return "";
                    } catch (Exception e) {
                        log.warn("SSE数据解析失败: {}", rawData);
                        return "";
                    }
                })
                .filter(chunk -> !chunk.isEmpty()) // 过滤空片段
                .doOnNext(chunk -> {
                    if (chunk.contains("<ref>")) {
                        log.info("检测到知识引用: {}", chunk);
                    }
                    fullResponse.append(chunk);
                })
                .doOnComplete(() -> {
                    // 保存完整对话记录
                    saveChatRecord(sessionId, userId, fullResponse.toString(), false);
                    log.info("RAG流式对话完成");
                })
                .onErrorResume(e -> {
                    log.error("RAG服务异常: {} {}", e.getMessage(),e.toString());
                    return Flux.just("【系统提示】知识库服务暂时不可用，请稍后重试");
                })
                .timeout(Duration.ofSeconds(300)); // 超时控制
    }

    // 格式化知识库引用
    private String formatDocReferences(JsonNode references) {
        StringBuilder sb = new StringBuilder("\n【知识来源】");
        references.forEach(ref -> {
            sb.append("\n- ").append(ref.path("doc_name").asText())
                    .append(": ").append(ref.path("text").asText().substring(0, 100)).append("...");
        });
        return sb.toString();
    }


    @Override
    public String ragSearch(Long sessionId, String message, Long userId) {
        // 构建符合API规范的请求体
        Map<String, Object> requestBody = new HashMap<>();

        // 输入参数结构
        Map<String, Object> input = new HashMap<>();
        input.put("prompt", message);
        requestBody.put("input", input);

        // 参数配置
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("has_thoughts", true);  // 需要获取检索过程

        // RAG配置（根据实际知识库ID修改）
        Map<String, Object> ragOptions = new HashMap<>();
        ragOptions.put("pipeline_ids", Collections.singletonList("wwzvnuysgw")); // 替换实际知识库ID
        ragOptions.put("top_k", 1); // 返回最相关的一条
        parameters.put("rag_options", ragOptions);

        requestBody.put("parameters", parameters);

        // 调试参数（可选）
        requestBody.put("debug", Collections.emptyMap());

        try {
            JsonNode response = dashscopeClient.post()
                    .uri("/api/v1/apps/{appId}/completion", "fe21741cec104e6a83226e989de14eb6") // 修正URI路径
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block(Duration.ofSeconds(15));

            return parseKnowledgeResponse(response);
        } catch (Exception e) {
            log.error("知识库搜索失败: {}", e.getMessage());
            return "知识检索服务暂时不可用，错误原因：" + e.getMessage();
        }
    }

    private String parseKnowledgeResponse(JsonNode response) {
        if (response == null) return "未获取到有效响应";

        // 检查错误响应
        if (response.has("code") && response.get("code").asInt() != 200) {
            String errorMsg = response.path("message").asText();
            log.error("API返回错误: {}", errorMsg);
            return "服务异常：" + errorMsg;
        }

        // 解析知识库引用
        JsonNode docs = response.path("output").path("doc_references");
        if (docs.isArray() && docs.size() > 0) {
            // 获取第一个文档的完整文本
            JsonNode firstDoc = docs.get(0);
            StringBuilder result = new StringBuilder();

            result.append("【文档名称】").append(firstDoc.path("doc_name").asText()).append("\n");
            result.append("【匹配内容】").append(firstDoc.path("text").asText());

            return result.toString();
        }
        return "未找到相关知识点";
    }
}