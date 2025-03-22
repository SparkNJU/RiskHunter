import { axios } from '../utils/request'
import { SIGNAL_MODULE } from './_prefix'
import { getRealTimeExchangeRate, getProcessedDailyRates } from '../utils/getExchangeRate'
import { getCurrencyPairById, type ExchangeRateVO } from '../types/signal'


// 获取汇率数据
export const getExchangeRate = async (currencyPair: number) => {
  // 测试可行
  // try {
  //   const pairConfig = getCurrencyPairById(currencyPair);

  //   if (!pairConfig) {
  //     throw new Error(`Invalid currency pair ID: ${currencyPair}`);
  //   }

  //   const realTimeData = await getRealTimeExchangeRate(
  //     pairConfig.fromCurrency,
  //     pairConfig.toCurrency
  //   );
  //   console.log(realTimeData)

  //   const dailyRates = await getProcessedDailyRates(
  //     pairConfig.fromCurrency,
  //     pairConfig.toCurrency,
  //     7
  //   );

  //   const latestRate = parseFloat(realTimeData['Realtime Currency Exchange Rate']['5. Exchange Rate']);
  //   const previousDayRate = dailyRates[dailyRates.length - 2]?.rate || latestRate;
  //   const change24h = latestRate - previousDayRate;

  //   const rateValues = dailyRates.map(d => d.rate);
  //   const mean = rateValues.reduce((sum, val) => sum + val, 0) / rateValues.length;
  //   const variance = rateValues.reduce((sum, val) => sum + Math.pow(val - mean, 2), 0) / rateValues.length;
  //   const volatility7d = Math.sqrt(variance);

  //   const volatilityPercentile = Math.min(Math.round(volatility7d * 1000), 100);

  //   const exchangeRateData: ExchangeRateVO = {
  //     currencyPair,
  //     currentRate: latestRate,
  //     change24h,
  //     volatility7d,
  //     volatilityPercentile,
  //     upcomingEvents: [{
  //       description: '欧洲央行利率决议',
  //       expectedImpact: 3
  //     }]
  //   };

  //   return Promise.resolve({
  //     data: exchangeRateData
  //   });
  // } catch (error) {
  //   console.error('Error fetching real-time exchange rate:', error);
  // }

  // 假数据
  return Promise.resolve({
    data: {
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
    }
  })
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

  // return Promise.resolve({
  //   data: {
  //     terms: [
  //       { currency: 0, range: '<30天', amount: 500, riskLevel: 30 },
  //       { currency: 0, range: '30-90天', amount: 800, riskLevel: 45 },
  //       { currency: 0, range: '>90天', amount: 1200, riskLevel: 75 },
  //       { currency: 1, range: '<30天', amount: 300, riskLevel: 25 },
  //       { currency: 1, range: '30-90天', amount: 550, riskLevel: 40 },
  //       { currency: 1, range: '>90天', amount: 700, riskLevel: 60 },
  //       { currency: 2, range: '<30天', amount: 200, riskLevel: 20 },
  //       { currency: 2, range: '30-90天', amount: 350, riskLevel: 35 },
  //       { currency: 2, range: '>90天', amount: 450, riskLevel: 50 }
  //     ]
  //   }
  // })
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
