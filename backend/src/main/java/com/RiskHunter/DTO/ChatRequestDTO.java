package com.RiskHunter.DTO;/*
 * @date 03/02 16:14
 */

import lombok.Data;

@Data
public class ChatRequestDTO {
    private Long sessionId;
    private String message;
    private Long userId;
}