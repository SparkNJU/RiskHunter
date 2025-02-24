package com.RiskHunter.DTO;/*
 * @date 02/23 18:14
 */

import lombok.Data;

import java.time.LocalDateTime;

// 查询DTO
@Data
public class RiskSignalQueryDTO {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer baseCurrency;
    private Integer targetCurrency;
    private Double minEmp;
    private Double maxEmp;
    private Double minExchangeRate;
    private String keyword;
    private Integer page = 1;
    private Integer size = 10;
}