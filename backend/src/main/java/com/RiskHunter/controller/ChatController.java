package com.RiskHunter.controller;/*
                                  * @date 03/02 15:18
                                  */

import com.RiskHunter.DTO.ChatRequestDTO;
import com.RiskHunter.po.ChatRecord;
import com.RiskHunter.po.ChatSession;
import com.RiskHunter.service.ChatService;
import com.RiskHunter.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.util.List;

@Slf4j
@RestController
@Api(value = "智能助手交互", tags = { "智能助手相关接口" })
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    /**
     * 创建会话接口
     * 
     * @param userId 用户唯一标识符（必须大于0）
     * @return ResultVO<Long> 包含新创建会话ID的响应对象
     * @apiNote 前端请求示例：
     *          POST /session?userId=12345
     */
    @ApiOperation(value = "创建会话接口", notes = "创建一个新的会话")
    @PostMapping("/session")
    public ResultVO<Long> createSession(
            @ApiParam(value = "用户唯一标识符（必须大于0）") @RequestParam Long userId) {
        return ResultVO.buildSuccess(chatService.createSession(userId));
    }
    // , produces = MediaType.TEXT_EVENT_STREAM_VALUE
    @ApiOperation(value = "流式聊天接口", notes = "通过流式方式进行聊天")
    @GetMapping(path = "/stream")
    public Flux<ServerSentEvent<String>> streamChat(
            @ApiParam(value = "会话ID") @RequestParam("sessionId") Long sessionId,
            @ApiParam(value = "用户ID") @RequestParam("userId") Long userId,
            @ApiParam(value = "消息内容") @RequestParam("message") String message,
            @ApiParam(value = "模型名称", required = false) @RequestParam(value = "modelName", required = false) String modelName) {
        log.info("sessionId: {}, userId: {}, message: {}", sessionId, userId, message);
        // 将请求参数封装成 DTO
        // ChatRequestDTO chatRequestDTO = new ChatRequestDTO();
        // chatRequestDTO.setSessionId(sessionId);
        // chatRequestDTO.setUserId(userId);
        // chatRequestDTO.setMessage(message);

        /*
            * 可选的modelName：
            * deepseek-r1（默认）
            * qwq-plus-latest
            * deepseek-v3
            * deepseek-r1-distill-qwen-32b
         */
        modelName = modelName == null ? "" : modelName;
        return chatService.chatWithStream(sessionId, message, userId,modelName)
                .map(chunk -> ServerSentEvent.<String>builder()
                        .data(chunk)
                        .build());
    }

    @ApiOperation(value = "非流式聊天接口", notes = "通过非流式方式进行聊天")
    @PostMapping("/noStream")
    public ResultVO<String> noStreamChat(
            @ApiParam(value = "聊天请求DTO") @RequestBody ChatRequestDTO chatRequestDTO) {
        Long sessionId = chatRequestDTO.getSessionId();
        String message = chatRequestDTO.getMessage();
        Long userId = chatRequestDTO.getUserId();
        return ResultVO.buildSuccess(chatService.chatWithoutStream(sessionId, message, userId));
    }



    //0313
    // RAG流式对话接口
    @ApiOperation(value = "RAG流式对话接口", notes = "通过RAG流式方式进行对话")
    @GetMapping(path = "/ragChat")
    public Flux<ServerSentEvent<String>> ragChatStream(
            @ApiParam(value = "会话ID") @RequestParam("sessionId") Long sessionId,
            @ApiParam(value = "用户ID") @RequestParam("userId") Long userId,
            @ApiParam(value = "消息内容") @RequestParam("message") String message) {
        log.info("RAG Chat sessionId: {}, userId: {}, message: {}", sessionId, userId, message);
        return chatService.ragChatWithStream(sessionId, message, userId)
                .map(chunk -> ServerSentEvent.<String>builder()
                        .data(chunk)
                        .build());
    }

    // RAG知识库搜索接口
    @ApiOperation(value = "RAG知识库搜索接口", notes = "通过RAG方式进行知识库搜索")
    @PostMapping("/ragSearch")
    public ResultVO<String> ragSearch(
            @ApiParam(value = "聊天请求DTO") @RequestBody ChatRequestDTO chatRequestDTO) {
        return ResultVO.buildSuccess(chatService.ragSearch(
                chatRequestDTO.getSessionId(),
                chatRequestDTO.getMessage(),
                chatRequestDTO.getUserId()));
    }

    @ApiOperation(value = "获取历史记录", notes = "获取指定会话的历史记录")
    @GetMapping("/history/{sessionId}")
    public ResultVO<List<ChatRecord>> getHistory(
            @ApiParam(value = "会话ID") @PathVariable Long sessionId,
            @ApiParam(value = "用户ID") @RequestParam Long userId) {
        return ResultVO.buildSuccess(chatService.getHistory(sessionId, userId));
    }

    /**
     * 获取用户的所有会话ID
     * 
     * @param userId 用户ID
     * @return 该用户所有会话ID的列表
     * @apiNote 前端请求示例：
     *          GET /sessions?userId=12345
     *  前端收到数据示例：
     *  {
     *   "code": 200,
     *   "message": "success", // 或者其他成功消息
     *   "data": [
     *     {
     *       "id": 1,
     *       "userId": 12345,
     *       "title": "关于保险的会话",
     *       "createTime": "2024-02-03T15:00:00",
     *       "updateTime": "2024-02-03T15:30:00"
     *     },
     *     {
     *       "id": 2,
     *       "userId": 12345,
     *       "title": "投资理财咨询",
     *       "createTime": "2024-02-02T10:00:00",
     *       "updateTime": "2024-02-02T12:00:00"
     *     }
     *   ]
     * }
     */
    @ApiOperation(value = "获取用户的所有会话ID", notes = "获取指定用户的所有会话ID")
    @GetMapping("/sessions")
    public ResultVO<List<ChatSession>> getUserSessions(
            @ApiParam(value = "用户ID") @RequestParam Long userId) {
        log.info("Getting all sessions for userId: {}", userId);
        return ResultVO.buildSuccess(chatService.getSessionsByUserId(userId));
    }

    /**
     * 更新会话标题
     * 
     * @param sessionId 会话ID
     * @param userId    用户ID
     * @param title     新标题
     * @return 更新结果
     * @apiNote 前端请求示例：
     *          PUT /session/123/title?userId=12345&title=新标题
     */
    @ApiOperation(value = "更新会话标题", notes = "更新指定会话的标题")
    @PutMapping("/session/{sessionId}/title")
    public ResultVO<Boolean> updateSessionTitle(
            @ApiParam(value = "会话ID") @PathVariable Long sessionId,
            @ApiParam(value = "用户ID") @RequestParam Long userId,
            @ApiParam(value = "新标题") @RequestParam String title) {
        return ResultVO.buildSuccess(chatService.updateSessionTitle(sessionId, userId, title));
    }


    @ApiOperation(value = "删除会话", notes = "删除指定的会话")
    @DeleteMapping("/session/{sessionId}")
    public ResultVO<Boolean> deleteSession(
            @ApiParam(value = "会话ID") @PathVariable Long sessionId,
            @ApiParam(value = "用户ID") @RequestParam Long userId) {
        log.info("Deleting session with ID: {}, for user: {}", sessionId, userId);
        return ResultVO.buildSuccess(chatService.deleteSession(sessionId, userId));
    }
}