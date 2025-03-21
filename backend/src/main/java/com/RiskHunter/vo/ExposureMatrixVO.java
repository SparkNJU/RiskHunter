package com.RiskHunter.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExposureMatrixVO {
    private List<Term> terms;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Term {
        private int currency;
        private String range;
        private double amount;
        private int riskLevel;
    }
}