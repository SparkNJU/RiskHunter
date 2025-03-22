<script setup lang="ts">
import { ref, watch } from 'vue';
import { currencyPairList, type ExchangeRateVO } from '../../types/signal';
import { getExchangeRate } from '../../api/signal';
import { TrendCharts, Warning, DataBoard } from '@element-plus/icons-vue';

const selectedCurrency = ref(0);
const rateData = ref<ExchangeRateVO>();

const loading = ref(false);

const loadData = async () => {
  try {
    loading.value = true;
    getExchangeRate(selectedCurrency.value).then((res: any) => {
      rateData.value = res.data;
    });
  } finally {
    loading.value = false;
  }
};
loadData()
watch(() => selectedCurrency.value, loadData);

const getVolatilityLevel = (percentile: number) => {
  if (percentile >= 70) return 'danger';
  if (percentile >= 40) return 'warning';
  return 'success';
};

const getImpactLevel = (impact: number) => {
  if (impact >= 3) return 'danger';
  if (impact >= 2) return 'warning';
  return 'info';
};
</script>

<template>
  <el-card class="exchange-rate-board" v-loading="loading">
    <template #header>
      <div class="panel-header">
        <h3 class="panel-title">
          <el-icon class="header-icon">
            <DataBoard />
          </el-icon>
          汇率看板
        </h3>
        <el-select v-model="selectedCurrency" placeholder="选择货币对" class="currency-select">
          <el-option v-for="item in currencyPairList" :key="item.id" :label="item.label" :value="item.id" />
        </el-select>
      </div>
    </template>

    <div v-if="rateData" class="rate-content">
      <div class="current-rate">
        <div class="rate-value">{{ rateData.currentRate.toFixed(4) }}</div>
        <div class="rate-change" :class="`change-${rateData.change24h >= 0 ? 'success' : 'danger'}`">
          ({{ rateData.change24h >= 0 ? '+' : '' }}{{ (rateData.change24h * 100).toFixed(2) }}% 24H)
        </div>
      </div>

      <div class="volatility-section">
        <div class="volatility-label">
          <el-icon>
            <TrendCharts />
          </el-icon>
          近7日波动率:
        </div>
        <div class="volatility-bar">
          <div class="heat-bar">
            <div class="heat-indicator" :style="{ width: `${rateData.volatilityPercentile}%` }"
              :class="`heat-${getVolatilityLevel(rateData.volatilityPercentile)}`"></div>
          </div>
          <div class="volatility-value">
            <el-tag :type="getVolatilityLevel(rateData.volatilityPercentile)" size="small">
              {{ (rateData.volatility7d * 100).toFixed(1) }}%
            </el-tag>
            <span class="percentile">(高于历史{{ rateData.volatilityPercentile }}%分位)</span>
          </div>
        </div>
      </div>

      <div class="upcoming-events">
        <div class="event-header">
          <el-icon>
            <Warning />
          </el-icon>
          事件标记:
        </div>
        <div class="event-list">
          <div v-for="(event, index) in rateData.upcomingEvents" :key="index" class="event-item">
            <el-tag :type="getImpactLevel(event.expectedImpact)" size="small" class="event-tag">
              {{ event.expectedImpact >= 3 ? '高' : event.expectedImpact >= 2 ? '中' : '低' }}
            </el-tag>
            {{ event.description }}
          </div>
        </div>
      </div>
    </div>
  </el-card>
</template>

<style scoped>
.exchange-rate-board {
  height: 100%;
}

.currency-select {
  width: 120px;
}

.rate-content {
  display: flex;
  flex-direction: column;
  gap: 16px;
  padding: 8px 0;
}

.current-rate {
  display: flex;
  align-items: baseline;
  gap: 8px;
}

.rate-value {
  font-size: 2.2rem;
  font-weight: bold;
  color: var(--el-text-color-primary);
}

.rate-change {
  font-size: 1rem;
}

.change-success {
  color: var(--el-color-success);
}

.change-danger {
  color: var(--el-color-danger);
}

.volatility-section {
  margin-top: 4px;
}

.volatility-label {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
}

.heat-bar {
  height: 8px;
  background-color: var(--el-fill-color-light);
  border-radius: 4px;
  overflow: hidden;
  margin-bottom: 8px;
}

.heat-indicator {
  height: 100%;
  border-radius: 4px;
}

.heat-danger {
  background-color: var(--el-color-danger);
}

.heat-warning {
  background-color: var(--el-color-warning);
}

.heat-success {
  background-color: var(--el-color-success);
}

.volatility-value {
  display: flex;
  align-items: center;
  gap: 8px;
}

.percentile {
  font-size: 0.85rem;
  color: var(--el-text-color-secondary);
}

.upcoming-events {
  border-top: 1px solid var(--el-border-color-lighter);
  padding-top: 16px;
  margin-top: 8px;
}

.event-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
  font-weight: 500;
}

.event-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.event-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.event-tag {
  flex-shrink: 0;
}
</style>