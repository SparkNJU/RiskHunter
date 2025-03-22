export interface NewsItem {
    id: number;
    title: string;
    source: string;
    date: string;
    impact: number; // 1-5 影响程度
    url?: string;
  }
  
  export interface RegionData {
    riskLevel: number; // 1-5 风险等级
    currencyPair: number; // 货币对编号
    currentRate: number; // 实时汇率
    rateChange: number; // 汇率变化百分比
    hotNews: NewsItem[]; // 热点新闻
    suggestions: string[]; // 建议
    lat?: number; // 纬度 (用于定位)
    lng?: number; // 经度 (用于定位)
    countryName?: string; // 国家名称
  }
  
  export interface RiskMapVO {
    regions: {
      [regionCode: number]: RegionData
    }
  }
  
  // 货币对的标识，例如 "1_2" 表示 CNY/USD
  export type CurrencyPairKey = string;
  
  // 将区域代码映射到国家/地区
  export const regionCodeToCountry: {[code: number]: {name: string, lat: number, lng: number}} = {
    1: { name: '中国', lat: 35.8617, lng: 104.1954 },
    2: { name: '美国', lat: 37.0902, lng: -95.7129 },
    3: { name: '欧元区', lat: 50.8503, lng: 4.3517 }, // 布鲁塞尔作为欧元区代表
    4: { name: '日本', lat: 36.2048, lng: 138.2529 },
    5: { name: '英国', lat: 55.3781, lng: -3.4360 },
    6: { name: '澳大利亚', lat: -25.2744, lng: 133.7751 },
    7: { name: '香港', lat: 22.3193, lng: 114.1694 },
    8: { name: '瑞士', lat: 46.8182, lng: 8.2275 }
  };