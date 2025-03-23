<script setup lang="ts">
import { computed, ref } from 'vue';
import { Aim, Histogram, Connection, Warning } from '@element-plus/icons-vue';
import { getForecast, getAdvice } from '../../api/signal';

const loading = ref(false);

const forecast = ref({
  range: [0, 0],
  confidence: 0,
  warnings: []
})
const advice = ref<any>([]);

// Format exchange rate to 2 decimal places
const formattedRange = computed(() => {
  return forecast.value.range.map(val => val.toFixed(2));
});

// Calculate midpoint for visualization
const midpoint = computed(() => {
  return ((forecast.value.range[0] + forecast.value.range[1]) / 2).toFixed(2);
});

// Calculate range width for visualization
const rangeWidth = computed(() => {
  return (forecast.value.range[1] - forecast.value.range[0]).toFixed(2);
});

const loadData = async () => {
  try {
    loading.value = true
    getAdvice().then((res: any) => {
      advice.value = res.data
    })

    getForecast().then((res: any) => {
      forecast.value = res.data
    })
  } finally {
    loading.value = false
  }
}

loadData()
</script>

<template>
  <el-card class="prediction-advice-card">
    <template #header>
      <div class="panel-header">
        <h3 class="panel-title">
          <el-icon class="header-icon">
            <Aim />
          </el-icon>
          预测与建议
        </h3>
      </div>
    </template>

    <el-row :gutter="20">
      <el-col :span="12">
        <div class="section-header">
          <el-icon class="section-icon">
            <Histogram />
          </el-icon>
          <h4 class="section-title">汇率概率预测 (USD-CNY)</h4>
        </div>

        <div class="prediction-summary">
          <div class="prediction-visual">
            <div class="range-indicator">
              <div class="range-bar">
                <div class="range-fill" :style="{ width: `${forecast.confidence}%` }"></div>
                <div class="range-midpoint" :style="{ left: `${forecast.confidence / 2}%` }">
                  {{ midpoint }}
                </div>
              </div>
              <div class="range-labels">
                <span class="range-min">{{ formattedRange[0] }}</span>
                <span class="range-max">{{ formattedRange[1] }}</span>
              </div>
            </div>
          </div>

          <div class="summary-item">
            <div class="summary-label">未来3个月区间:</div>
            <div class="summary-value highlight">
              {{ formattedRange[0] }} - {{ formattedRange[1] }}
              <span class="confidence-note">({{ forecast.confidence }}%置信区间)</span>
            </div>
          </div>
          <div class="summary-item">
            <div class="summary-label">预计波动幅度:</div>
            <div class="summary-value">
              <el-tag type="info" size="small">{{ rangeWidth }} CNY</el-tag>
            </div>
          </div>
        </div>

        <div class="warnings-section">
          <div class="warning-header">
            <el-icon class="warning-icon">
              <Warning />
            </el-icon>
            <h5 class="warning-title">预警</h5>
          </div>
          <el-alert v-for="(warning, index) in forecast.warnings" :key="index" :title="warning" type="warning"
            :closable="false" show-icon class="warning-alert" />
        </div>
      </el-col>

      <el-col :span="12">
        <div class="section-header">
          <el-icon class="section-icon">
            <Connection />
          </el-icon>
          <h4 class="section-title">智能建议</h4>
        </div>

        <div class="recommendations">
          <div v-for="(rec, index) in advice" :key="index" class="recommendation-group">
            <h5 class="recommendation-title">
              <el-tag :type="rec.type === 'immediate' ? 'danger' : 'success'" size="small">
                {{ rec.title }}
              </el-tag>
            </h5>
            <ul class="recommendation-list">
              <li v-for="(item, i) in rec.items" :key="i" class="recommendation-item">
                {{ item }}
              </li>
            </ul>
          </div>
        </div>
      </el-col>
    </el-row>
  </el-card>
</template>

<style scoped>
.prediction-advice-card {
  width: 100%;
}

.prediction-summary {
  margin-top: 16px;
  padding: 15px;
  background-color: var(--el-fill-color-light);
  border-radius: 6px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);

  .prediction-visual {
    margin-bottom: 20px;
    padding: 15px;
    background-color: var(--el-fill-color-lighter);
    border-radius: 6px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  }
}

.section-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 16px;
}

.section-icon {
  color: var(--el-color-primary);
  font-size: 18px;
}

.section-title {
  font-size: 1.3rem;
  font-weight: 600;
  margin: 0;
  color: var(--el-text-color-primary);
}

.prediction-chart {
  height: 300px;
  width: 100%;
}

.summary-item {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
}

.summary-item:last-child {
  margin-bottom: 0;
}


.summary-label {
  width: 120px;
  font-weight: 600;
  color: var(--el-text-color-regular);
}

.summary-value {
  flex: 1;
  color: var(--el-text-color-primary);
}

.confidence-note,
.probability-note {
  font-size: 12px;
  color: var(--el-text-color-secondary);
  margin-left: 4px;
}

.recommendations {
  height: 350px;
  overflow-y: auto;
}

.recommendation-group {
  margin-bottom: 20px;
}

.recommendation-title {
  margin: 0 0 12px 0;
  font-weight: 600;
  font-size: 15px;
}

.recommendation-list {
  padding-left: 20px;
  margin: 0;
}

.recommendation-item {
  margin-bottom: 10px;
  line-height: 1.5;
  color: var(--el-text-color-regular);
}

.range-indicator {
  width: 100%;
  margin-top: 10px;
  margin-bottom: 5px;

  .range-bar {
    position: relative;
    height: 12px;
    background-color: var(--el-fill-color);
    border-radius: 6px;
    margin-bottom: 10px;
  }

  .range-fill {
    position: absolute;
    height: 100%;
    background: linear-gradient(90deg, rgba(64, 158, 255, 0.2) 0%, rgba(64, 158, 255, 0.8) 100%);
    border-radius: 6px;
  }

  .range-midpoint {
    position: absolute;
    top: -25px;
    transform: translateX(-50%);
    background-color: var(--el-color-primary);
    color: white;
    padding: 2px 8px;
    border-radius: 4px;
    font-size: 14px;
    font-weight: bold;
  }

  .range-midpoint::after {
    content: '';
    position: absolute;
    top: 100%;
    left: 50%;
    transform: translateX(-50%);
    border-width: 5px;
    border-style: solid;
    border-color: var(--el-color-primary) transparent transparent transparent;
  }

  .range-labels {
    display: flex;
    justify-content: space-between;
    font-size: 14px;
    color: var(--el-text-color-secondary);
  }
}

.highlight {
  font-weight: bold;
  color: var(--el-color-primary);
  font-size: 16px;
}

.warnings-section {
  margin-top: 20px;
  padding: 15px;
  background-color: rgba(245, 108, 108, 0.05);
  border-radius: 6px;
  border-left: 4px solid var(--el-color-warning);
}

.warning-header {
  display: flex;
  align-items: center;
  gap: 6px;
  margin-bottom: 12px;
}

.warning-icon {
  color: var(--el-color-warning);
}

.warning-title {
  margin: 0;
  font-size: 16px;
  color: var(--el-color-warning-dark-2);
}

.warning-alert {
  margin-bottom: 8px;
}
</style>