import { axios } from '../utils/request'
import { SIGNAL_MODULE } from './_prefix'

import type {
  RiskMapVO,
  ExposureMatrixVO
} from '../types/signal'

const mockExchangeRateData = [
  {
    currencyPair: 0,
    currentRate: 6.8723,
    change24h: 0.0035,
    volatility7d: 0.082,
    volatilityPercentile: 80,
    upcomingEvents: [
      {
        description: '10月8日美国非农数据发布',
        expectedImpact: 3
      }
    ]
  },
  {
    currencyPair: 1,
    currentRate: 7.5241,
    change24h: -0.0025,
    volatility7d: 0.065,
    volatilityPercentile: 65,
    upcomingEvents: [
      {
        description: '欧洲央行利率决议',
        expectedImpact: 3
      }
    ]
  },
  {
    currencyPair: 2,
    currentRate: 8.9127,
    change24h: 0.0012,
    volatility7d: 0.048,
    volatilityPercentile: 45,
    upcomingEvents: [
      {
        description: '英国GDP公布',
        expectedImpact: 2
      }
    ]
  }
]

// 获取汇率数据
export const getExchangeRate = (currencyPair: number) => {
  // return axios.get(`${SIGNAL_MODULE}/exchange-rate/${currencyPair}`)

  return Promise.resolve({
    data: mockExchangeRateData.find(item => item.currencyPair === currencyPair)
  })
}

// 获取风险评分
export const getRiskScore = () => {
  // return axios.get(`${SIGNAL_MODULE}/risk-score`)

  return Promise.resolve({
    data: {
      name: '当前敞口',
      riskStatus: 'medium',
      score: 62,
      updateTime: new Date(),
      factorBreakdown: {
        volatility: 0.4,
        concentration: 0.3,
        hedgeGap: 0.2
      },
      trend: {
        score: 5,
        description: '受人民币贬值压力增加影响'
      }
    }
  })
}

// 获取预警列表
export const getAlerts = () => {
  // return axios.get(`${SIGNAL_MODULE}/alerts`)

  return Promise.resolve({
    data: [
      {
        level: 'urgent',
        title: '阿根廷比索单周贬值12%',
        content: '影响南美客户付款',
        updateTime: new Date()
      },
      {
        level: 'warning',
        title: '欧元区通胀超预期',
        content: 'ECB加息概率升至75%',
        updateTime: new Date()
      },
      {
        level: 'normal',
        title: 'USD/CNY 1M远期对冲',
        content: '对冲覆盖率达标（80%）',
        updateTime: new Date()
      }
    ]
  })
}

export const getRiskMap = () => {
  return Promise.resolve<RiskMapVO>({
    regions: {
      AR: {
        riskLevel: 4,
        currencyPair: "ARS/CNY",
        currentRate: 0.023,
        rateChange: -0.12,
        hotNews: [/* 新闻数据 */],
        suggestions: [
          "要求预付款比例提升至50%",
          "购买CDS对冲"
        ]
      }
    }
  })
}

export const getExposureMatrix = () => {
  return Promise.resolve({
    data: {
      terms: [
        { currency: 0, range: '<30天', amount: 500, riskLevel: 30 },
        { currency: 0, range: '30-90天', amount: 800, riskLevel: 45 },
        { currency: 0, range: '>90天', amount: 1200, riskLevel: 75 },
        { currency: 1, range: '<30天', amount: 300, riskLevel: 25 },
        { currency: 1, range: '30-90天', amount: 550, riskLevel: 40 },
        { currency: 1, range: '>90天', amount: 700, riskLevel: 60 },
        { currency: 2, range: '<30天', amount: 200, riskLevel: 20 },
        { currency: 2, range: '30-90天', amount: 350, riskLevel: 35 },
        { currency: 2, range: '>90天', amount: 450, riskLevel: 50 }
      ]
    }
  })
}

export const getForecast = () => {
  return Promise.resolve({
    data: {
      range: [6.65, 7.10],
      confidence: 80,
      warnings: ["若中美关税升级，可能突破7.20（概率5%）"]
    }
  })
}

export const getAdvice = () => {
  return Promise.resolve({
    data: [
      {
        type: 'immediate',
        title: '立即行动',
        items: [
          '与工行签订6个月远期合约（锁定汇率6.89）',
          '增加30%的外币资产配置以对冲风险',
          '控制贸易融资成本，减少使用信用证'
        ]
      },
      {
        type: 'long_term',
        title: '长期建议',
        items: [
          '调整欧元区客户账期至60天以内',
          '增加人民币计价出口合同比例',
          '采用跨币种净额结算减少外汇交易成本'
        ]
      }
    ]
  })
}
