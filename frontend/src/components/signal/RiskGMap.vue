<template>
    <div class="risk-map-container" :style="{ height }">
      <div ref="chartContainer" class="chart-container"></div>
    </div>
  </template>
  
  <script setup lang="ts">
  import { ref, onMounted, onUnmounted, watch } from 'vue';
  import * as echarts from 'echarts';
  // 导入Google Maps扩展
  import 'echarts-extension-gmap';
  import { regionCodeToCountry } from '../../types/risk-map';
  import type { RiskMapVO } from '../../types/risk-map';
  
  // 声明Google Maps类型，避免TS错误
  declare global {
    interface Window {
      google: any;
    }
  }
  
  // 配置Google Maps API密钥（需要从Google Cloud Console获取）
  const GOOGLE_MAP_API_KEY = 'YOUR_API_KEY';
  
  const props = defineProps({
    height: {
      type: String,
      default: '400px'
    }
  });
  
  const chartContainer = ref<HTMLElement | null>(null);
  let chart: echarts.ECharts | null = null;
  const riskMapData = ref<RiskMapVO | null>(null);
  
  // 将风险等级映射到颜色
  const getRiskColor = (level: number) => {
    const colors = [
      '#e0f3f8', // 最低风险
      '#abd9e9',
      '#74add1',
      '#4575b4',
      '#313695'  // 最高风险
    ];
    return colors[level - 1] || colors[0];
  };
  
  // 加载Google Maps脚本
  const loadGoogleMapsScript = (): Promise<void> => {
    return new Promise((resolve, reject) => {
      // 检查是否已经加载
      if (window.google && window.google.maps) {
        resolve();
        return;
      }
  
      // 创建脚本
      const script = document.createElement('script');
      script.src = `https://maps.googleapis.com/maps/api/js?key=${GOOGLE_MAP_API_KEY}`;
      script.async = true;
      script.defer = true;
      script.onload = () => resolve();
      script.onerror = () => reject(new Error('Google Maps加载失败'));
      document.head.appendChild(script);
    });
  };
  
  // 加载风险地图数据
  const loadRiskMapData = async () => {
    try {
      // 先加载Google Maps脚本
      await loadGoogleMapsScript();
      
      // 实际项目中，应该从API获取数据
      // 这里使用模拟数据
      const mockData: RiskMapVO = {
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
          // 新增南美国家数据
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
      };
      
      riskMapData.value = mockData;
      initializeChart();
    } catch (error) {
      console.error('加载风险地图数据失败:', error);
    }
  };
  
  // 初始化图表
  const initializeChart = () => {
    if (!chartContainer.value || !riskMapData.value) return;
    
    // 如果已经有实例，先销毁
    if (chart) {
      chart.dispose();
    }
    
    // 创建新实例
    chart = echarts.init(chartContainer.value);
    chart.showLoading({text: '加载中...'});
    
    // 准备国家/地区数据
    const mapData = Object.entries(riskMapData.value.regions).map(([code, data]) => {
      const regionInfo = regionCodeToCountry[Number(code)];
      if (!regionInfo) return null;
      
      // 在世界地图上的英文国家名
      const countryNameMap: {[key: string]: string} = {
        '中国': 'China',
        '美国': 'United States',
        '英国': 'United Kingdom',
        '日本': 'Japan',
        '澳大利亚': 'Australia',
        '瑞士': 'Switzerland',
        '阿根廷': 'Argentina',
        '巴西': 'Brazil',
        '欧元区': 'Germany',
        '香港': 'Hong Kong'
      };
      
      // 获取英文国家名
      const englishName = countryNameMap[regionInfo.name] || regionInfo.name;
      
      // 获取国家坐标（经纬度）
      const coordinates = [regionInfo.lng, regionInfo.lat];
      
      return {
        name: englishName,
        value: [...coordinates, data.riskLevel],  // [lng, lat, value]
        regionCode: Number(code),
        data: data
      };
    }).filter(item => item !== null);
    
    chart.hideLoading();
    
    // 设置地图配置项
    const option: echarts.EChartsOption = {
      backgroundColor: '#f5f5f5',
      title: {
        text: '全球金融风险热力图',
        subtext: '风险等级从低(1)到高(5)',
        left: 'center',
        top: '20px'
      },
      tooltip: {
        trigger: 'item',
        formatter: function(params: any) {
          const data = params.data;
          if (!data || !data.data) return '';
          
          const regionData = data.data;
          const regionInfo = regionCodeToCountry[data.regionCode];
          
          let html = `<div style="font-weight:bold;margin-bottom:5px;">${regionInfo?.name || '未知地区'}</div>`;
          html += `<div>风险等级: <span style="color:${getRiskColor(regionData.riskLevel)}">${regionData.riskLevel}</span>/5</div>`;
          html += `<div>当前汇率: ${regionData.currentRate}</div>`;
          html += `<div>汇率变化: <span style="color:${regionData.rateChange >= 0 ? 'green' : 'red'}">${regionData.rateChange >= 0 ? '+' : ''}${regionData.rateChange}%</span></div>`;
          
          if (regionData.hotNews && regionData.hotNews.length > 0) {
            html += `<div style="margin-top:5px;font-weight:bold;">最新动态:</div>`;
            html += `<div>${regionData.hotNews[0].title}</div>`;
          }
          
          if (regionData.suggestions && regionData.suggestions.length > 0) {
            html += `<div style="margin-top:5px;font-weight:bold;">建议:</div>`;
            html += `<div>${regionData.suggestions[0]}</div>`;
          }
          
          return html;
        }
      },
      visualMap: {
        type: 'piecewise',
        pieces: [
          { min: 1, max: 1, label: '极低', color: '#e0f3f8' },
          { min: 2, max: 2, label: '低', color: '#abd9e9' },
          { min: 3, max: 3, label: '中', color: '#74add1' },
          { min: 4, max: 4, label: '高', color: '#4575b4' },
          { min: 5, max: 5, label: '极高', color: '#313695' }
        ],
        orient: 'horizontal',
        left: 'center',
        bottom: '30px',
        text: ['高风险', '低风险'],
        calculable: false,
        dimension: 2  // 指定数据的第三个维度为视觉映射维度
      },
      gmap: {
        center: [0, 40],  // 初始地图中心点
        zoom: 2,          // 初始缩放级别
        renderOnMoving: true,
        roam: true,      // 允许缩放和平移
        echartsLayerZIndex: 2019,
        mapStyle: {      // 自定义地图样式，可选
          styleJson: [
            {
              "featureType": "all",
              "elementType": "all",
              "stylers": { "visibility": "on" }
            }
          ]
        }
      },
      series: [
        {
          name: '风险等级',
          type: 'scatter',
          coordinateSystem: 'gmap',  // 使用gmap坐标系
          data: mapData,
          symbolSize: function(val: any) {
            return val[2] * 8;  // 根据风险等级调整大小
          },
          encode: {
            value: 2,  // 指定数据中第3个值为value
            lng: 0,    // 经度是第1个值
            lat: 1     // 纬度是第2个值
          },
          itemStyle: {
            color: function(params: any) {
              const value = params.value;
              if (Array.isArray(value) && value.length > 2) {
                return getRiskColor(value[2]);
              }
              return '#e0f3f8';
            }
          }
        }
      ]
    };
    
    chart.setOption(option);
    
    // 获取Google Map实例进行额外配置
    // 使用任意类型避免TypeScript错误
    const gmapComponent = (chart as any).getModel().getComponent('gmap');
    if (gmapComponent && typeof gmapComponent.getGoogleMap === 'function') {
      //const gmap = gmapComponent.getGoogleMap();
      // 可以添加额外的谷歌地图配置
      console.log('Google Map 实例加载成功');
    }
  };
  
  // 处理窗口大小变化
  const handleResize = () => {
    chart?.resize();
  };
  
  // 监听组件属性变化
  watch(() => props.height, () => {
    setTimeout(() => {
      handleResize();
    }, 300);
  });
  
  onMounted(() => {
    loadRiskMapData();
    window.addEventListener('resize', handleResize);
  });
  
  onUnmounted(() => {
    if (chart) {
      chart.dispose();
      chart = null;
    }
    window.removeEventListener('resize', handleResize);
  });
  </script>
  
  <style scoped>
  .risk-map-container {
    width: 100%;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    border-radius: 4px;
    overflow: hidden;
    background-color: #fff;
  }
  
  .chart-container {
    width: 100%;
    height: 100%;
  }
  </style>