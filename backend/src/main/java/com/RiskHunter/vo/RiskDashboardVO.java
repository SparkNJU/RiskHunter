package com.RiskHunter.vo;

import com.RiskHunter.po.RiskFactor;
import com.RiskHunter.po.ScoreTrend;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RiskDashboardVO {
    private String name;
    private String riskStatus;
    private double score;
    private String updateTime;
    private List<RiskFactor> factorBreakdown;
    private ScoreTrend trend;
}

