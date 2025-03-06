package com.RiskHunter.controller;/*
                                  * @date 03/02 15:18
                                  */

import com.RiskHunter.DTO.ChatRequestDTO;
import com.RiskHunter.po.ChatRecord;
import com.RiskHunter.po.ChatSession;
import com.RiskHunter.service.ChatService;
import com.RiskHunter.vo.ResultVO;
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
    @PostMapping("/session")
    public ResultVO<Long> createSession(@RequestParam Long userId) {
        return ResultVO.buildSuccess(chatService.createSession(userId));
    }

    // , produces = MediaType.TEXT_EVENT_STREAM_VALUE
    @GetMapping(path = "/stream")
    public Flux<ServerSentEvent<String>> streamChat(
            @RequestParam("sessionId") Long sessionId,
            @RequestParam("userId") Long userId,
            @RequestParam("message") String message) {
        log.info("sessionId: {}, userId: {}, message: {}", sessionId, userId, message);
        // 将请求参数封装成 DTO
        // ChatRequestDTO chatRequestDTO = new ChatRequestDTO();
        // chatRequestDTO.setSessionId(sessionId);
        // chatRequestDTO.setUserId(userId);
        // chatRequestDTO.setMessage(message);

        return chatService.chatWithStream(sessionId, message, userId)
                .map(chunk -> ServerSentEvent.<String>builder()
                        .data(chunk)
                        .build());
    }

    @PostMapping("/noStream")
    public ResultVO<String> noStreamChat(@RequestBody ChatRequestDTO chatRequestDTO) {
        Long sessionId = sessionId = chatRequestDTO.getSessionId();
        String message = chatRequestDTO.getMessage();
        Long userId = chatRequestDTO.getUserId();
        return ResultVO.buildSuccess(chatService.chatWithoutStream(sessionId, message, userId));
    }

    @GetMapping("/history/{sessionId}")
    public ResultVO<List<ChatRecord>> getHistory(
            @PathVariable Long sessionId,
            @RequestParam Long userId) {
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
    @GetMapping("/sessions")
    public ResultVO<List<ChatSession>> getUserSessions(@RequestParam Long userId) {
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
    @PutMapping("/session/{sessionId}/title")
    public ResultVO<Boolean> updateSessionTitle(
            @PathVariable Long sessionId,
            @RequestParam Long userId,
            @RequestParam String title) {
        return ResultVO.buildSuccess(chatService.updateSessionTitle(sessionId, userId, title));
    }
    

}