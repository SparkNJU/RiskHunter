package com.RiskHunter.service;

import com.RiskHunter.po.ChatRecord;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

public interface ChatService {
    public Long createSession(Long userId);
    public String chat(Long sessionId, String message, Long userId);
    public List<ChatRecord> getHistory(Long sessionId, Long userId);
}