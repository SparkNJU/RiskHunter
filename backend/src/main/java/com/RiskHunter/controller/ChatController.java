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

    @PostMapping("/session")
    public ResultVO<Long> createSession(@RequestParam Long userId) {
        return ResultVO.buildSuccess(chatService.createSession(userId));
    }

    @PostMapping("/stream")
    public ResultVO<String> streamChat(@RequestBody ChatRequestDTO chatRequestDTO) {
        Long sessionId = 0L;
        if (chatRequestDTO.getSessionId() == null) {
            sessionId = chatService.createSession(chatRequestDTO.getUserId());
        }
        else{
            sessionId = chatRequestDTO.getSessionId();
        }
        String message = chatRequestDTO.getMessage();
        Long userId = chatRequestDTO.getUserId();
        return ResultVO.buildSuccess(chatService.chat(sessionId, message, userId));
    }

    @GetMapping("/history/{sessionId}")
    public ResultVO<List<ChatRecord>> getHistory(
            @PathVariable Long sessionId,
            @RequestHeader Long userId) {
        return ResultVO.buildSuccess(chatService.getHistory(sessionId, userId));
    }
}