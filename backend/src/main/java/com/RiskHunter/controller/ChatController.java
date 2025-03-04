package com.RiskHunter.controller;/*
 * @date 03/02 15:18
 */

import com.RiskHunter.DTO.ChatRequestDTO;
import com.RiskHunter.po.ChatRecord;
import com.RiskHunter.po.ChatSession;
import com.RiskHunter.service.ChatService;
import com.RiskHunter.vo.ResultVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    /**
     * 创建会话接口
     * @param userId 用户唯一标识符（必须大于0）
     * @return ResultVO<Long> 包含新创建会话ID的响应对象
     * @apiNote 前端请求示例：
     * POST /session?userId=12345
     */
    @PostMapping("/session")
    public ResultVO<Long> createSession(@RequestParam Long userId) {
        return ResultVO.buildSuccess(chatService.createSession(userId));
    }
    /**

     * 聊天接口 虽然api名字叫stream，但是实际上是一个同步的接口
     * @param chatRequestDTO 聊天请求数据传输对象（JSON格式）
     *        JSON结构要求
     *        {
     *          "sessionId": 会话ID（必须大于0）,
     *          "message": "用户输入的聊天内容",
     *          "userId": 用户ID（必须与session所属用户一致）
     *        }
     * @return ResultVO<String> 包含AI回复内容的响应对象
     * @apiNote 前端请求示例：
     * POST /stream
     * Request Body:
     * {
     *   "sessionId": 67890,
     *   "message": "如何学习Java编程？",
     *   "userId": 12345
     * }

     */
    @PostMapping("/stream")
    public ResultVO<String> streamChat(@RequestBody ChatRequestDTO chatRequestDTO) {
        Long sessionId = sessionId = chatRequestDTO.getSessionId();
        String message = chatRequestDTO.getMessage();
        Long userId = chatRequestDTO.getUserId();
        return ResultVO.buildSuccess(chatService.chat(sessionId, message, userId));
    }

    @GetMapping("/history/{sessionId}")
    public ResultVO<List<ChatRecord>> getHistory(
            @PathVariable Long sessionId,
            @RequestParam Long userId) {
        return ResultVO.buildSuccess(chatService.getHistory(sessionId, userId));
    }
}