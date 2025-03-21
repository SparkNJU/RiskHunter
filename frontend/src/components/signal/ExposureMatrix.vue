<script setup lang="ts">
import { ref,  nextTick } from 'vue';
import * as echarts from 'echarts';
import { type ExposureMatrixVO } from '../../types/signal';
import { Money } from '@element-plus/icons-vue';
import { CurrencyList, parseCurrencyName } from '../../utils';
import { getExposureMatrix } from '../../api/signal';

const chartRef = ref<HTMLElement | null>(null);
let chartInstance: echarts.ECharts | null = null;

const loading = ref(false);

const exposureData = ref<ExposureMatrixVO[]>();

const currencyNames = CurrencyList.map(c => parseCurrencyName(c.number));
const termRanges = ['<30天', '30-90天', '>90天'];

const getTermRangeIndex = (range: string) => {
  const days = parseInt(range.replace(/[^0-9]/g, ''));
  if (days < 30) return 0;
  if (days <= 90) return 1;
  return 2;
};

const processData = () => {
  const data: (string | number)[][] = [];

  if (!exposureData.value) return data;

  // 合并所有terms数据
  const allTerms = exposureData.value.flatMap(d => d.terms);

  for (const term of allTerms) {
    data.push([
      term.currency - 1,
      getTermRangeIndex(term.range),
      term.amount / 1000,                   // 保持千美元单位转换
      term.riskLevel * 33,                   // 将1-3级风险转换为33-99数值
      parseCurrencyName(term.currency),
      term.range.replace('天', '天'),        // 统一中文格式
      `${(term.amount / 1000).toFixed(1)}千美元`,
      ['低', '中', '高'][term.riskLevel - 1] // 风险等级转中文
    ]);
  }
  console.log(allTerms)

  return data;
};

const initBubbleChart = () => {
  if (!chartRef.value) return;

  if (chartInstance) {
    chartInstance.dispose();
  }

  chartInstance = echarts.init(chartRef.value);

  const option = {
    tooltip: {
      formatter: function (params: any) {
        return `
          <div style="font-weight:bold;">${params.data[4]}</div>
          <div>账期: ${params.data[5]}</div>
          <div>敞口金额: ${params.data[6]}</div>
          <div>风险等级: ${params.data[7]}（${params.data[3].toFixed(0)}/100）</div>
        `;
      }
    },
    grid: {
      left: '10%',
      right: '10%',
      top: '10%',
      bottom: '15%'
    },
    xAxis: {
      type: 'category',
      data: currencyNames,
      nameGap: 25,
      axisLine: {
        lineStyle: {
          color: '#999'
        }
      },
      axisLabel: {
        color: '#666',
        fontSize: 12
      }
    },
    yAxis: {
      type: 'category',
      data: termRanges,
      splitLine: {
        show: false
      },
      axisLine: {
        lineStyle: {
          color: '#999'
        }
      },
      axisLabel: {
        color: '#666',
        fontSize: 12
      }
    },
    visualMap: {
      min: 0,
      max: 100,
      dimension: 3,
      inRange: {
        color: ['#67C23A', '#E6A23C', '#F56C6C']
      },
      textStyle: {
        color: '#666'
      },
      left: 'right',
      text: ['高风险', '低风险'],
      calculable: true
    },
    series: [
      {
        name: '敞口分析',
        type: 'scatter',
        symbolSize: function (val: any) {
          return Math.sqrt(val[2]) * 2;
        },
        data: processData(),
        animationDuration: 1000,
        animationEasing: 'cubicInOut',
        animationDelay: function (idx: number) {
          return idx * 100;
        },
        itemStyle: {
          borderColor: '#fff',
          borderWidth: 1
        },
        emphasis: {
          itemStyle: {
            borderWidth: 2,
            shadowBlur: 10,
            shadowColor: 'rgba(0, 0, 0, 0.3)'
          }
        }
      }
    ]
  };

  chartInstance.setOption(option);

  window.addEventListener('resize', () => {
    chartInstance?.resize();
  });
};

const loadData = async () => {
  try {
    loading.value = true
    getExposureMatrix().then((res: any) => {
      console.log(res.data)
      exposureData.value = res.data
      nextTick(() => {
        initBubbleChart();
      });
    })
  } finally {
    loading.value = false
  }
};

loadData()
</script>

<template>
  <el-card class="exposure-matrix-card">
    <template #header>
      <div class="panel-header">
        <h3 class="panel-title">
          <el-icon class="header-icon">
            <Money />
          </el-icon>
          敞口分析
        </h3>
      </div>
    </template>

    <div class="matrix-description">
      <div class="legend-item">
        <span class="bubble-size-indicator"></span>
        <span class="legend-text">气泡大小 = 敞口金额（万美元）</span>
      </div>
      <div class="legend-item">
        <span class="color-indicator"></span>
        <span class="legend-text">颜色深浅 = 风险等级</span>
      </div>
    </div>

    <div ref="chartRef" class="matrix-container"></div>
  </el-card>
</template>

<style scoped>
.exposure-matrix-card {
  height: 100%;
}

.matrix-description {
  display: flex;
  justify-content: center;
  gap: 24px;
  margin-bottom: 16px;
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
  color: var(--el-text-color-secondary);
}

.bubble-size-indicator {
  width: 16px;
  height: 16px;
  border-radius: 50%;
  border: 1px solid #ccc;
  background-color: rgba(103, 194, 58, 0.2);
}

.color-indicator {
  width: 50px;
  height: 12px;
  background: linear-gradient(to right, #67C23A, #E6A23C, #F56C6C);
  border-radius: 2px;
}

.matrix-container {
  height: 400px;
  width: 100%;
}
</style>