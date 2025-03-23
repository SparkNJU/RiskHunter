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



        // Customer 1
        List<ExposureMatrixVO.Term> customer1 = new ArrayList<>();
        customer1.add(new ExposureMatrixVO.Term(1,"30天", 275000, 3));
        customer1.add(new ExposureMatrixVO.Term(1,"120天", 410000, 1));


        // Customer 2
        List<ExposureMatrixVO.Term> customer2 = new ArrayList<>();
        customer2.add(new ExposureMatrixVO.Term(2,"60天", 150000, 2));

        // Customer 3
        List<ExposureMatrixVO.Term> customer3 = new ArrayList<>();
        customer3.add(new ExposureMatrixVO.Term(3,"90天", 380000, 2));
        customer3.add(new ExposureMatrixVO.Term(3,"30天", 120000, 1));
        customer3.add(new ExposureMatrixVO.Term(3,"180天", 500000, 3));

        // Customer 4
        List<ExposureMatrixVO.Term> customer4 = new ArrayList<>();
        customer4.add(new ExposureMatrixVO.Term(4,"60天", 320000, 3));

        // Customer 5
        List<ExposureMatrixVO.Term> customer5 = new ArrayList<>();
        customer5.add(new ExposureMatrixVO.Term(5,"30天", 450000, 2));
        customer5.add(new ExposureMatrixVO.Term(5,"90天", 200000, 1));

        // Customer 6
        List<ExposureMatrixVO.Term> customer6 = new ArrayList<>();
        customer6.add(new ExposureMatrixVO.Term(6,"120天", 180000, 1));
        customer6.add(new ExposureMatrixVO.Term(6,"30天", 350000, 2));


        // Customer 7
        List<ExposureMatrixVO.Term> customer7 = new ArrayList<>();
        customer7.add(new ExposureMatrixVO.Term(7,"60天", 290000, 1));
        customer7.add(new ExposureMatrixVO.Term(7,"180天", 85000, 3));
        customer7.add(new ExposureMatrixVO.Term(7,"90天", 420000, 2));


        // Customer 8
        List<ExposureMatrixVO.Term> customer8 = new ArrayList<>();
        customer8.add(new ExposureMatrixVO.Term(8,"30天", 100000, 2));


        result.add(ExposureMatrixVO.builder().terms(customer1).build());
        result.add(ExposureMatrixVO.builder().terms(customer2).build());
        result.add(ExposureMatrixVO.builder().terms(customer3).build());
        result.add(ExposureMatrixVO.builder().terms(customer4).build());
        result.add(ExposureMatrixVO.builder().terms(customer5).build());
        result.add(ExposureMatrixVO.builder().terms(customer6).build());
        result.add(ExposureMatrixVO.builder().terms(customer7).build());
        result.add(ExposureMatrixVO.builder().terms(customer8).build());

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