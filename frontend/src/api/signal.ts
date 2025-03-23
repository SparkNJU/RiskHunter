import { axios } from '../utils/request'
import { SIGNAL_MODULE } from './_prefix'
import { getRealTimeExchangeRate, getProcessedDailyRates } from '../utils/getExchangeRate'
import { getCurrencyPairById, type ExchangeRateVO } from '../types/signal'


const mockExchangeRate = [
  {
    currencyPair: 0,
    currentRate: 7.2481,
    change24h: 0.0014,
    volatility7d: 0.011,
    volatilityPercentile: 11,
    upcomingEvents: [
      {
        description: '3月22日急贬近百点后次日强势反弹0.58%',
        expectedImpact: 3
      },
      {
        description: '美元指数下行周期下人民币双向波动加剧',
        expectedImpact: 2
      }
    ]
  },
  {
    currencyPair: 1,
    currentRate: 7.8398,
    change24h: -0.0158,
    volatility7d: 0.023,
    volatilityPercentile: 23,
    upcomingEvents: [
      {
        description: '2025年初欧元区刺激政策推升人民币兑欧元汇率',
        expectedImpact: 2
      },
      {
        description: '中欧贸易顺差扩大支撑人民币汇率稳定',
        expectedImpact: 1
      }
    ]
  },
  {
    currencyPair: 2,
    currentRate: 9.3629,
    change24h: 0.0311,
    volatility7d: 0.016,
    volatilityPercentile: 16,
    upcomingEvents: [
      {
        description: '英镑兑人民币汇率突破9.3关口，留学成本飙升20%',
        expectedImpact: 1
      },
      {
        description: '英国经济复苏预期推动英镑短期走强',
        expectedImpact: 3
      }
    ]
  }, {
    currencyPair: 3,
    currentRate: 0.0485,
    change24h: -0.0001,
    volatility7d: 0.0,
    volatilityPercentile: 0,
    upcomingEvents: [
      {
        description: '日元汇率跌破5.0大关引发中国资本涌入日本资产',
        expectedImpact: 3
      },
      {
        description: '日本央行维持宽松政策加剧日元贬值压力',
        expectedImpact: 1
      }
    ]
  },
  {
    currencyPair: 4,
    currentRate: 4.5471,
    change24h: -0.0134,
    volatility7d: 0.025,
    volatilityPercentile: 25,
    upcomingEvents: [
      {
        description: '美联储降息预期引发澳元兑人民币汇率暴跌至两年新低',
        expectedImpact: 3
      },
      {
        description: '澳洲铁矿出口疲软加剧澳元贬值压力',
        expectedImpact: 1
      }
    ]
  },
  {
    currencyPair: 5,
    currentRate: 0.9320,
    change24h: 0.0,
    volatility7d: 0.001,
    volatilityPercentile: 1,
    upcomingEvents: [
      {
        description: '港元兑人民币汇率突破0.93，北上消费热潮持续',
        expectedImpact: 2
      },
      {
        description: '央行逆周期调节抑制港元汇率单边波动',
        expectedImpact: 3
      }
    ]
  },
  {
    currencyPair: 6,
    currentRate: 8.2060,
    change24h: -0.0090,
    volatility7d: 0.021,
    volatilityPercentile: 21,
    upcomingEvents: [
      {
        description: '瑞士央行意外降息冲击人民币汇率',
        expectedImpact: 3
      },
      {
        description: '避险需求推升瑞郎兑人民币中间价年内下调147基点',
        expectedImpact: 1
      }
    ]
  }
]

// 获取汇率数据
export const getExchangeRate = async (currencyPair: number) => {
  // 最新假数据
  // return Promise.resolve({
  //   data: mockExchangeRate.find((item) => item.currencyPair === currencyPair)
  // })

  try {
    const pairConfig = getCurrencyPairById(currencyPair);

    if (!pairConfig) {
      throw new Error(`Invalid currency pair ID: ${currencyPair}`);
    }

    const realTimeData = await getRealTimeExchangeRate(
      pairConfig.fromCurrency,
      pairConfig.toCurrency
    );

    const dailyRates = await getProcessedDailyRates(
      pairConfig.fromCurrency,
      pairConfig.toCurrency,
      7
    );

    const latestRate = parseFloat(realTimeData['Realtime Currency Exchange Rate']['5. Exchange Rate']);
    const previousDayRate = dailyRates[dailyRates.length - 2]?.rate || latestRate;
    const change24h = latestRate - previousDayRate;

    const rateValues = dailyRates.map(d => d.rate);
    const mean = rateValues.reduce((sum, val) => sum + val, 0) / rateValues.length;
    const variance = rateValues.reduce((sum, val) => sum + Math.pow(val - mean, 2), 0) / rateValues.length;
    const volatility7d = Math.sqrt(variance);

    const volatilityPercentile = Math.min(Math.round(volatility7d * 1000), 100);

    const exchangeRateData: ExchangeRateVO = {
      currencyPair,
      currentRate: latestRate,
      change24h,
      volatility7d,
      volatilityPercentile,
      upcomingEvents: mockExchangeRate.find((item) => item.currencyPair === currencyPair)?.upcomingEvents || []
    };

    return Promise.resolve({
      data: exchangeRateData
    });
  } catch (error) {
    // console.error('Error fetching real-time exchange rate:', error);
    return Promise.resolve({
      data: mockExchangeRate.find((item) => item.currencyPair === currencyPair)
    })
  }
};

// 获取风险评分
export const getRiskScore = () => {
  return axios.get(`${SIGNAL_MODULE}/dashboard`)

  // return Promise.resolve({
  //   data: {
  //     name: '当前敞口',
  //     riskStatus: 'medium',
  //     score: 62,
  //     updateTime: new Date(),
  //     factorBreakdown: {
  //       volatility: 0.4,
  //       concentration: 0.3,
  //       hedgeGap: 0.2
  //     },
  //     trend: {
  //       score: 5,
  //       description: '受人民币贬值压力增加影响'
  //     }
  //   }
  // })
}

// 获取预警列表
export const getAlerts = () => {
  return axios.get(`${SIGNAL_MODULE}/alert`)

  // return Promise.resolve({
  //   data: [
  //     {
  //       level: 'urgent',
  //       title: '阿根廷比索单周贬值12%',
  //       content: '影响南美客户付款',
  //       updateTime: new Date()
  //     },
  //     {
  //       level: 'warning',
  //       title: '欧元区通胀超预期',
  //       content: 'ECB加息概率升至75%',
  //       updateTime: new Date()
  //     },
  //     {
  //       level: 'normal',
  //       title: 'USD/CNY 1M远期对冲',
  //       content: '对冲覆盖率达标（80%）',
  //       updateTime: new Date()
  //     }
  //   ]
  // })
}

export const getRiskMap = () => {
  // return axios.get(`${SIGNAL_MODULE}/map`)

  return Promise.resolve({
    data: {
      regions: {
        1: { // 中国
          riskLevel: 2,
          currencyPair: 1,
          currentRate: 7.1243,
          rateChange: -0.42,
          hotNews: [
            { id: 1, title: '央行公布新一轮LPR利率', source: '央行网站', date: '2025-03-20', impact: 4 }
          ],
          suggestions: ['监控人民币汇率变化', '考虑增加美元储备']
        },
        2: { // 美国
          riskLevel: 3,
          currencyPair: 2,
          currentRate: 1.0,
          rateChange: 0,
          hotNews: [
            { id: 2, title: '美联储暗示年内可能降息', source: '彭博社', date: '2025-03-19', impact: 5 }
          ],
          suggestions: ['关注美联储政策动向', '减少美元敞口']
        },
        3: { // 欧元区
          riskLevel: 4,
          currencyPair: 3,
          currentRate: 0.9235,
          rateChange: -1.25,
          hotNews: [
            { id: 3, title: '欧元区通胀率升至5年高位', source: 'ECB报告', date: '2025-03-18', impact: 5 }
          ],
          suggestions: ['减少欧元计价资产', '购买通胀保值债券']
        },
        4: { // 日本
          riskLevel: 1,
          currencyPair: 4,
          currentRate: 155.23,
          rateChange: 2.15,
          hotNews: [
            { id: 4, title: '日本央行维持宽松货币政策', source: '日经新闻', date: '2025-03-21', impact: 3 }
          ],
          suggestions: ['考虑日元融资', '增加日本国债持有']
        },
        5: { // 英国
          riskLevel: 3,
          currencyPair: 5,
          currentRate: 0.7843,
          rateChange: -0.56,
          hotNews: [
            { id: 5, title: '英国零售销售数据好于预期', source: '英国统计局', date: '2025-03-15', impact: 3 }
          ],
          suggestions: ['关注英国贸易政策变化', '评估脱欧后续影响']
        },
        6: { // 澳大利亚
          riskLevel: 2,
          currencyPair: 6,
          currentRate: 1.5241,
          rateChange: 0.32,
          hotNews: [
            { id: 6, title: '澳大利亚GDP增长超出预期', source: '澳大利亚统计局', date: '2025-03-17', impact: 4 }
          ],
          suggestions: ['考虑增加澳大利亚资源股投资', '关注中澳贸易关系']
        },
        7: { // 香港
          riskLevel: 5,
          currencyPair: 7,
          currentRate: 7.8124,
          rateChange: -0.05,
          hotNews: [
            { id: 7, title: '香港金管局跟随美联储加息', source: '香港金管局', date: '2025-03-20', impact: 4 }
          ],
          suggestions: ['监控联系汇率稳定性', '评估香港作为金融中心地位变化']
        },
        8: { // 瑞士
          riskLevel: 1,
          currencyPair: 8,
          currentRate: 0.9012,
          rateChange: 0.75,
          hotNews: [
            { id: 8, title: '瑞士央行维持负利率政策', source: '瑞士央行公告', date: '2025-03-16', impact: 3 }
          ],
          suggestions: ['考虑瑞士法郎作为避险资产', '关注瑞士银行业变化']
        },
        9: { // 阿根廷
          riskLevel: 5,
          currencyPair: 9,
          currentRate: 366.45,
          rateChange: 115.2,
          hotNews: [
            { id: 9, title: '阿根廷通胀率达115%创新高', source: '路透社', date: '2025-03-14', impact: 5 }
          ],
          suggestions: ['要求预付款比例提升至50%', '购买CDS对冲风险']
        },
        10: { // 巴西
          riskLevel: 3,
          currencyPair: 10,
          currentRate: 5.08,
          rateChange: -2.35,
          hotNews: [
            { id: 10, title: '巴西央行维持基准利率', source: '彭博社', date: '2025-03-15', impact: 4 }
          ],
          suggestions: ['关注巴西国内政治局势', '评估大宗商品价格波动影响']
        }
      }
    }
  })
}

export const getExposureMatrix = () => {
  return axios.get(`${SIGNAL_MODULE}/exposure`)
}

export const getForecast = () => {
  return Promise.resolve({
    data: {
      range: [7.20, 7.35],
      confidence: 80,
      warnings: [
        "美联储政策转向或美元指数反弹可能加剧人民币波动",
        "中美贸易摩擦升级（如特朗普关税政策落地）或导致阶段性贬值压力",
        "国际资本流动变化可能引发汇率短期超调风险"
      ]
    }
  })
}

export const getAdvice = () => {
  return Promise.resolve({
    data: [
      {
        "type": "immediate",
        "title": "立即行动",
        "items": [
          "【做空美元/离岸人民币】结合中国央行稳汇率政策（如逆周期因子调节），可短线做空USD/CNH至7.28，止损设于7.30上方",
          "【关注美联储政策信号】3月25日美联储主席鲍威尔讲话若重申“维持高利率”，美元指数或上探104.5阻力位，建议在数据公布前买入美元/瑞郎（USD/CHF）对冲波动风险",
          "【日元波段交易】建议在151.50-152.50区间高抛低吸，同时买入152.00看跌期权对冲干预风险"
        ]
      },
      {
        "type": "long_term",
        "title": "长期建议",
        "items": [
          "【分散资产配置】建议配置20%-30%美元资产，同时增加黄金（XAU/USD）及日元对冲美元波动，避免单一货币敞口过大",
          "【布局人民币资产】中国GDP增速预期5%且外汇储备稳定，可关注A股科技板块（受益国产AI爆发）及国债等人民币计价资产的长期收益"
        ]
      }
    ]
  })
}
