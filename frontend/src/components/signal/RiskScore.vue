<script setup lang="ts">
import { ref } from 'vue';
import * as echarts from 'echarts';
import { type RiskScoreVO } from '../../types/signal';
import { ArrowUp, ArrowDown, Star } from '@element-plus/icons-vue';
import { getRiskScore } from '../../api/signal';

const chartRef = ref<HTMLElement | null>(null);
let chartInstance: echarts.ECharts | null = null;

const loading = ref(false);

const riskData = ref<RiskScoreVO>({
  name: '',
  riskStatus: 'low',
  score: 62,
  updateTime: '',
  factorBreakdown: [],
  trend: {
    value: 0,
    direction: ''
  }
});

const initGaugeChart = () => {
  if (!chartRef.value || !riskData.value) return;

  if (chartInstance) {
    chartInstance.dispose();
  }

  chartInstance = echarts.init(chartRef.value);

  const option = {
    series: [
      {
        type: 'gauge',
        startAngle: 180,
        endAngle: 0,
        center: ['50%', '75%'],
        radius: '90%',
        min: 0,
        max: 100,
        splitNumber: 10,
        axisLine: {
          lineStyle: {
            width: 6,
            color: [
              [0.3, '#67C23A'],
              [0.7, '#E6A23C'],
              [1, '#F56C6C']
            ]
          }
        },
        pointer: {
          icon: 'path://M12.8,0.7l12,40.1H0.7L12.8,0.7z',
          length: '12%',
          width: 10,
          offsetCenter: [0, '-60%'],
          itemStyle: {
            color: 'auto'
          }
        },
        axisTick: {
          length: 12,
          lineStyle: {
            color: 'auto',
            width: 2
          }
        },
        splitLine: {
          length: 20,
          lineStyle: {
            color: 'auto',
            width: 2
          }
        },
        axisLabel: {
          color: '#999',
          fontSize: 10,
          distance: -40,
          formatter: function (value: number) {
            if (value === 0 || value === 100) {
              return value + '';
            }
            return '';
          }
        },
        title: {
          offsetCenter: [0, '-20%'],
          fontSize: 20
        },
        detail: {
          valueAnimation: true,
          formatter: '{value}',
          color: 'inherit',
          fontSize: 30,
          offsetCenter: [0, '0%']
        },
        data: [
          {
            value: riskData.value.score.toFixed(0),
            name: '风险评分',
            title: {
              color: riskData.value.score > 70 ? '#F56C6C' :
                riskData.value.score > 30 ? '#E6A23C' : '#67C23A'
            }
          }
        ]
      }
    ]
  };

  // Apply options
  chartInstance.setOption(option);

  // Handle resize
  window.addEventListener('resize', () => {
    chartInstance?.resize();
  });
};

const loadData = async () => {
  try {
    loading.value = true
    getRiskScore().then((res: any) => {
      riskData.value = res.data
      initGaugeChart()
    })

  } finally {
    loading.value = false
  }
}

loadData()
</script>

<template>
  <el-card class="risk-score-card" v-loading="loading">
    <template #header>
      <div class="panel-header">
        <h3 class="panel-title">
          <el-icon class="header-icon">
            <Star />
          </el-icon>
          风险评分
        </h3>
      </div>
    </template>

    <div class="risk-score-content">
      <div ref="chartRef" class="gauge-chart"></div>

      <div class="risk-breakdo wn">
        <h4 class="breakdown-title">风险分解</h4>
        <div class="factor-list">
          <div v-for="(factor, index) in riskData.factorBreakdown" :key="index" class="factor-item">
            <div class="factor-name">
              {{ factor.name }}
            </div>
            <el-progress :percentage="factor.value * 100" :format="(percentage: number) => `${percentage.toFixed(0)}%`"
              :stroke-width="8" class="factor-progress" />
          </div>
        </div>
      </div>

      <div class="risk-trend">
        <div class="trend-header">
          <div class="trend-title">趋势</div>
          <div class="trend-indicator">
            <el-icon v-if="riskData.trend.direction === 'up'" color="#F56C6C">
              <ArrowUp />
            </el-icon>
            <el-icon v-else color="#67C23A">
              <ArrowDown />
            </el-icon>
            <span :class="riskData.trend.direction === 'up' ? 'trend-up' : 'trend-down'">
              {{ riskData.trend.direction === 'up' ? '+' : '-' }}{{ riskData.trend.value }}分
            </span>
          </div>
        </div>
        <div class="trend-description">
          <!-- {{ riskData.trend.description }} -->
        </div>
      </div>
    </div>
  </el-card>
</template>

<style scoped>
.risk-score-card {
  height: 100%;
}

.risk-score-content {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.gauge-chart {
  height: 200px;
  width: 100%;
}

.risk-breakdown {
  margin-top: 8px;
}

.breakdown-title {
  font-size: 16px;
  margin-bottom: 12px;
  font-weight: 600;
  color: var(--el-text-color-primary);
}

.factor-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.factor-item {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
}

.factor-name {
  width: 80px;
  flex-shrink: 0;
  font-size: 14px;
  color: var(--el-text-color-regular);
}

.factor-progress {
  flex-grow: 1;
}

.risk-trend {
  border-top: 1px solid var(--el-border-color-lighter);
  padding-top: 16px;
  margin-top: 8px;
}

.trend-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.trend-title {
  font-size: 16px;
  font-weight: 600;
  color: var(--el-text-color-primary);
}

.trend-indicator {
  display: flex;
  align-items: center;
  gap: 4px;
}

.trend-up {
  color: var(--el-color-danger);
}

.trend-down {
  color: var(--el-color-success);
}

.trend-description {
  font-size: 14px;
  color: var(--el-text-color-regular);
}
</style>