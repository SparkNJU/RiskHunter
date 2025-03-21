import type { NewsItem } from "./news";

export const CurrencyPairs = [
    'USD/CNY', 'EUR/CNY', 'GBP/CNY'
]

export const currencyOptions = [
  { value: 0, label: 'USD/CNY' },
  { value: 1, label: 'EUR/CNY' },
  { value: 2, label: 'GBP/CNY' }
]

export interface EventMarker {
    description: string;
    expectedImpact: number;
}

export interface RiskFactor {
    volatility: number;
    concentration: number;
    hedgeGap: number;
}

export interface ScoreTrend {
    score: number; // 相对分数
    description: string; // 原因描述
}

// 汇率波动
export interface ExchangeRateVO {
    data: ExchangeRateVO | undefined;
    currencyPair: number;
    currentRate: number;
    change24h: number;
    volatility7d: number;
    volatilityPercentile: number;
    upcomingEvents: EventMarker[];
}

// 风险评分
export interface RiskScoreVO {
    name: string;
    riskStatus: 'low' | 'medium' | 'high';
    score: number;
    updateTime: Date;
    factorBreakdown: RiskFactor;
    trend: ScoreTrend;
}

// 预警信息
export interface AlertVO {
    level: 'urgent' | 'warning' | 'normal';
    title: string;
    content: string;
    updateTime: Date;
}

// 风险地图数据
export interface RiskMapVO {
    regions: {
        [regionCode: string]: {
            riskLevel: number;
            currencyPair: string;
            currentRate: number;
            rateChange: number;
            hotNews: NewsItem[];
            suggestions: string[];
        }
    }
}

// 敞口分析
export interface ExposureMatrixVO {
    terms: {
        currency: number;
        range: string;
        amount: number;
        riskLevel: number;
    }[];
}