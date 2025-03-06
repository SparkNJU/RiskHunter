package com.RiskHunter.service;

import com.RiskHunter.po.ChatRecord;
import com.RiskHunter.po.ChatSession;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.util.List;

public interface ChatService {
    public Long createSession(Long userId);

    public Flux<String> chatWithStream(Long sessionId, String message, Long userId);

    public String chatWithoutStream(Long sessionId, String message, Long userId);

    public List<ChatRecord> getHistory(Long sessionId, Long userId);

    public List<ChatSession> getSessionsByUserId(Long userId);

    public Boolean updateSessionTitle(Long sessionId, Long userId, String title);
}