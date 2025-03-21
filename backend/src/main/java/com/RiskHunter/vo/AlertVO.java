package com.RiskHunter.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertVO {
    private String level;
    private String title;
    private String content;
    private String updateTime;
}