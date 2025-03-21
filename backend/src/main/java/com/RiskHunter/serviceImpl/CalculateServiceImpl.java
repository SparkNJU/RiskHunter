package com.RiskHunter.serviceImpl;

import com.RiskHunter.po.RiskFactor;
import com.RiskHunter.po.ScoreTrend;
import com.RiskHunter.service.CalculateService;
import com.RiskHunter.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class CalculateServiceImpl implements CalculateService {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public RiskDashboardVO getRiskDashboard() {
        // 模拟从用户信息获取用户名
        String username = "Demo User";

        // 计算风险评分（这里使用模拟数据，实际应该根据风险信号计算）
        double score = calculateRiskScore();

        // 根据评分确定风险状态
        String riskStatus = determineRiskStatus(score);

        // 获取当前时间
        String updateTime = LocalDateTime.now().format(formatter);

        // 创建风险因子分解
        List<RiskFactor> factorBreakdown = Arrays.asList(
                new RiskFactor("汇率波动", 0.7),
                new RiskFactor("敞口集中度", 0.5),
                new RiskFactor("对冲缺口", 0.3)
        );

        // 模拟趋势计算（实际应该根据历史数据计算）
        ScoreTrend trend = new ScoreTrend(0.05, "up");

        return RiskDashboardVO.builder()
                .name(username)
                .riskStatus(riskStatus)
                .score(score)
                .updateTime(updateTime)
                .factorBreakdown(factorBreakdown)
                .trend(trend)
                .build();
    }

    @Override
    public AlertVO getAlert() {
        // 根据风险信号计算预警级别
        String level = calculateAlertLevel();

        // 生成预警标题和内容
        String title = generateAlertTitle(level);
        String content = generateAlertContent(level);

        // 获取当前时间
        String updateTime = LocalDateTime.now().format(formatter);

        return AlertVO.builder()
                .level(level)
                .title(title)
                .content(content)
                .updateTime(updateTime)
                .build();
    }

    @Override
    public RiskMapVO getRiskMap() {
        Map<Integer, RiskMapVO.RegionRisk> regions = new HashMap<>();

        // 添加不同地区的风险数据（模拟数据）
        regions.put(1, createRegionRisk(1, 2, 6.42, -0.02, 2));
        regions.put(2, createRegionRisk(2, 3, 0.89, 0.01, 1));
        regions.put(3, createRegionRisk(3, 4, 128.5, 0.5, 3));

        return RiskMapVO.builder().regions(regions).build();
    }

    @Override
    public List<ExposureMatrixVO> getExposure() {
        List<ExposureMatrixVO> result = new ArrayList<>();

        // 从用户数据获取敞口信息（模拟数据）
        List<ExposureMatrixVO.Term> terms1 = new ArrayList<>();
        terms1.add(createTerm(1, "30天", 500000, 2));
        terms1.add(createTerm(1, "60天", 300000, 1));

        List<ExposureMatrixVO.Term> terms2 = new ArrayList<>();
        terms2.add(createTerm(2, "30天", 400000, 3));
        terms2.add(createTerm(2, "90天", 200000, 2));

        result.add(ExposureMatrixVO.builder().terms(terms1).build());
        result.add(ExposureMatrixVO.builder().terms(terms2).build());

        return result;
    }

    // 辅助方法

    private double calculateRiskScore() {
        // 实际应根据风险信号计算
        // 这里简单模拟，返回0-100之间的随机值
        return 65 + Math.random() * 20;
    }

    private String determineRiskStatus(double score) {
        if (score < 40) return "low";
        else if (score < 70) return "medium";
        else return "high";
    }

    private String calculateAlertLevel() {
        // 模拟根据风险信号计算预警级别
        double random = Math.random();
        if (random < 0.2) return "urgent";
        else if (random < 0.6) return "warning";
        else return "normal";
    }

    private String generateAlertTitle(String level) {
        switch (level) {
            case "urgent": return "紧急风险预警：汇率大幅波动风险";
            case "warning": return "风险提示：市场波动增加";
            default: return "日常提示：市场状况正常";
        }
    }

    private String generateAlertContent(String level) {
        switch (level) {
            case "urgent": return "近期市场剧烈波动，预计汇率变化幅度可能超过3%，建议立即审视敞口状况并考虑对冲措施。";
            case "warning": return "市场波动性增加，主要货币对波动性位于60%历史分位数，建议关注敞口变化。";
            default: return "市场运行平稳，无特殊风险信号。";
        }
    }

    private RiskMapVO.RegionRisk createRegionRisk(int currencyPair, double currentRate, double rateChange, double change, int riskLevel) {
        List<RiskMapVO.NewsItem> news = new ArrayList<>();
        news.add(new RiskMapVO.NewsItem("市场动态标题", "https://example.com/news/1", "2023-05-15"));

        List<String> suggestions = new ArrayList<>();
        if (riskLevel > 2) {
            suggestions.add("建议降低敞口");
            suggestions.add("考虑增加对冲比例");
        } else if (riskLevel == 2) {
            suggestions.add("保持关注市场波动");
        } else {
            suggestions.add("维持当前风险管理策略");
        }

        return RiskMapVO.RegionRisk.builder()
                .currencyPair(currencyPair)
                .currentRate(currentRate)
                .rateChange(rateChange)
                .riskLevel(riskLevel)
                .hotNews(news)
                .suggestions(suggestions)
                .build();
    }

    private ExposureMatrixVO.Term createTerm(int currency, String range, double amount, int riskLevel) {
        return ExposureMatrixVO.Term.builder()
                .currency(currency)
                .range(range)
                .amount(amount)
                .riskLevel(riskLevel)
                .build();
    }
}