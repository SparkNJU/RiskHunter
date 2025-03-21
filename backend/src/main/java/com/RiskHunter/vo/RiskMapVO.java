package com.RiskHunter.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RiskMapVO {
    private Map<Integer, RegionRisk> regions;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegionRisk {
        private int riskLevel;
        private int currencyPair;
        private double currentRate;
        private double rateChange;
        private List<NewsItem> hotNews;
        private List<String> suggestions;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NewsItem {
        private String title;
        private String url;
        private String date;
    }
}