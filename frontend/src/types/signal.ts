
export interface CurrencyPairConfig {
  id: number;
  label: string;
  fromCurrency: number;
  toCurrency: number;
}

export const currencyPairList: CurrencyPairConfig[] = [
  { id: 0, label: 'USD/CNY', fromCurrency: 2, toCurrency: 1 },
  { id: 1, label: 'EUR/CNY', fromCurrency: 3, toCurrency: 1 },
  { id: 2, label: 'GBP/CNY', fromCurrency: 5, toCurrency: 1 },
  { id: 3, label: 'JPY/CNY', fromCurrency: 4, toCurrency: 1 },
  { id: 4, label: 'AUD/CNY', fromCurrency: 6, toCurrency: 1 },
  { id: 5, label: 'HKD/CNY', fromCurrency: 7, toCurrency: 1 },
  { id: 6, label: 'CHF/CNY', fromCurrency: 8, toCurrency: 1 }
];

export function getCurrencyPairById(id: number): CurrencyPairConfig | undefined {
  return currencyPairList.find(pair => pair.id === id);
}

export interface EventMarker {
    description: string;
    expectedImpact: number;
}

export interface RiskFactor {
   name: string;
   value: number;
}

export interface ScoreTrend {
    value: number;
    direction: string;
}

// 汇率波动
export interface ExchangeRateVO {
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
    updateTime: string;
    factorBreakdown: RiskFactor[];
    trend: ScoreTrend;
}

// 预警信息
export interface AlertVO {
    level: 'urgent' | 'warning' | 'normal';
    title: string;
    content: string;
    updateTime: string;
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