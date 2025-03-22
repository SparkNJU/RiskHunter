<template>
  <el-card v-loading="isMapLoading">
    <template #header>
      <div class="panel-header">
        <h3 class="panel-title">
          <el-icon class="header-icon">
            <MapLocation />
          </el-icon>
          风险地图
        </h3>
      </div>
    </template>

    <div class="risk-map-container" :style="{ height }">
      <div v-if="mapError" class="map-error">
        <el-alert title="地图加载失败" type="error" description="请检查地图数据是否正确" show-icon :closable="false" />
        <el-button @click="retryLoadMap" type="primary" size="small" style="margin-top: 10px">
          重试加载
        </el-button>
      </div>
      <div v-else ref="chartContainer" class="chart-container"></div>
    </div>
  </el-card>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, watch } from 'vue';
import { MapLocation } from '@element-plus/icons-vue';
import * as echarts from 'echarts';
import { regionCodeToCountry } from '../../types/risk-map';
import type { RiskMapVO } from '../../types/risk-map';
import { getRiskMap } from '../../api/signal';
// 直接导入本地GeoJSON数据
import worldGeoJson from '@surbowl/world-geo-json-zh/world.zh.json';

const props = defineProps({
  height: {
    type: String,
    default: '400px'
  }
});

const chartContainer = ref<HTMLElement | null>(null);
let chart: echarts.ECharts | null = null;
const riskMapData = ref<RiskMapVO | null>(null);
const mapError = ref(false);
const isMapLoading = ref(false);

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

// 重试加载地图
const retryLoadMap = () => {
  mapError.value = false;
  loadRiskMapData();
};

// 加载风险地图数据
const loadRiskMapData = async () => {
  try {
    mapError.value = false;
    isMapLoading.value = true;

    // 注册地图数据到echarts
    echarts.registerMap('world', worldGeoJson);

    getRiskMap().then((res: any) => {
      riskMapData.value = res.data
      initializeChart()
    })
  } catch (error) {
    console.error('加载风险地图数据失败:', error);
    mapError.value = true;
  } finally {
    isMapLoading.value = false
  }
};

// 初始化图表
const initializeChart = () => {
  if (!chartContainer.value) {
    console.error('图表容器不存在');
    return;
  }

  if (!riskMapData.value) {
    console.error('风险数据为空');
    return;
  }

  // 如果已经有实例，先销毁
  if (chart) {
    chart.dispose();
    chart = null;
  }

  console.log('开始初始化ECharts实例...');

  // 创建新实例，添加willReadFrequently优化属性
  chart = echarts.init(chartContainer.value, null, {
    renderer: 'canvas',
    useDirtyRect: true
  });

  chart.showLoading({ text: '加载中...' });

  // 准备国家/地区数据映射
  const countryDataMap: { [key: string]: any } = {};

  // 遍历风险数据，为每个国家/地区设置风险等级
  Object.entries(riskMapData.value.regions).forEach(([code, data]) => {
    const regionInfo = regionCodeToCountry[Number(code)];
    if (!regionInfo) return;

    // 在地图数据中找到对应的国家
    countryDataMap[regionInfo.name] = {
      value: data.riskLevel,
      regionCode: Number(code),
      ...data
    };
  });

  // 转换为ECharts需要的数据格式
  const seriesData = Object.keys(countryDataMap).map(name => ({
    name,
    value: countryDataMap[name].value,
    ...countryDataMap[name]
  }));

  console.log('处理后的地图数据:', seriesData);

  chart.hideLoading();

  // 设置地图配置项
  const option: echarts.EChartsOption = {
    backgroundColor: '#ffffff',
    title: {
      text: '全球金融风险热力图',
      subtext: '风险等级从低(1)到高(5)',
      left: 'center',
      top: '20px'
    },
    tooltip: {
      trigger: 'item',
      formatter: function (params: any) {
        if (!params.data || params.data.value === undefined) {
          return params.name;
        }

        const regionData = params.data;

        let html = `<div style="font-weight:bold;margin-bottom:5px;">${params.name}</div>`;
        html += `<div>风险等级: <span style="color:${getRiskColor(regionData.value)}">${regionData.value}</span>/5</div>`;

        if (regionData.currentRate !== undefined) {
          html += `<div>当前汇率: ${regionData.currentRate}</div>`;
        }

        if (regionData.rateChange !== undefined) {
          html += `<div>汇率变化: <span style="color:${regionData.rateChange >= 0 ? 'green' : 'red'}">${regionData.rateChange >= 0 ? '+' : ''}${regionData.rateChange}%</span></div>`;
        }

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
      calculable: false
    },
    // 如果有这个配置会多一层图层, 不知道这是不是feature
    // geo: {
    //   map: 'world',
    //   roam: true, // 允许缩放和平移
    //   zoom: 1.2, // 初始缩放级别
    //   scaleLimit: {
    //     min: 0.8,
    //     max: 5
    //   },
    //   label: {
    //     show: false
    //   },
    //   itemStyle: {
    //     areaColor: '#f7f7f7',
    //     borderColor: '#ccc',
    //     borderWidth: 0.5
    //   },
    //   emphasis: {
    //     label: {
    //       show: false
    //     },
    //     itemStyle: {
    //       areaColor: '#f5f5f5'
    //     }
    //   }
    // },
    series: [
      {
        name: '风险等级',
        type: 'map',
        map: 'world',
        roam: true,
        zoom: 1.2,
        scaleLimit: {
          min: 1.0,
          max: 5.0
        },
        center: [0, 15],
        data: seriesData,
        label: {
          show: false
        },
        emphasis: {
          label: {
            show: false
          }
        },
        // 设置地图选中样式
        select: {
          itemStyle: {
            color: '#eee'
          }
        }
      }
    ]
  };

  console.log('设置地图选项...');
  chart.setOption(option);

  // 添加鼠标滚轮缩放支持
  chartContainer.value!.addEventListener('mousewheel', () => {
    setTimeout(() => {
      chart?.resize();
    }, 100);
  });
};

// 处理窗口大小变化
const handleResize = () => {
  if (chart) {
    chart.resize();
  }
};

// 监听组件属性变化
watch(() => props.height, () => {
  setTimeout(() => {
    handleResize();
  }, 300);
});

onMounted(() => {
  // console.log('组件已挂载，开始加载风险地图数据');
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
  position: relative;
}

.chart-container {
  width: 100%;
  height: 100%;
}

.map-error {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  text-align: center;
  width: 80%;
}
</style>