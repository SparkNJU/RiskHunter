<script setup lang="ts">
import { ref } from 'vue';
import { Aim, Histogram, Connection } from '@element-plus/icons-vue';
import { getForecast, getAdvice } from '../../api/signal';

const loading = ref(false);

const forecast = ref({
  range:[0,0],
  confidence: 0,
  warnings: []
})
const advice = ref<any>([]);

const loadData = async () => {
  try {
    loading.value = true
    getAdvice().then((res: any) => {
      advice.value = res.data
    })

    getForecast().then((res: any) => {
      forecast.value = res.data
      console.log(forecast.value.range)
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
          <h4 class="section-title">汇率概率预测</h4>
        </div>

        <div class="prediction-summary">
          <div class="summary-item">
            <div class="summary-label">未来3个月区间:</div>
            <div class="summary-value">
              {{ forecast.range[0] }} - {{ forecast.range[1] }}
              <span class="confidence-note">({{ forecast.confidence }}%置信区间)</span>
            </div>
          </div>
          <div class="summary-item">
            <div class="summary-label">黑天鹅预警:</div>
            <div class="summary-value">
              <span v-for="(warning, index) in forecast.warnings" :key="index">
                {{ warning }}
              </span>
            </div>
          </div>
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

.prediction-summary {
  margin-top: 16px;
  padding: 12px;
  background-color: var(--el-fill-color-light);
  border-radius: 4px;
}

.summary-item {
  display: flex;
  margin-bottom: 8px;
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
</style>