<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { 
  getRealTimeExchangeRate, 
  getDailyExchangeRate, 
  getProcessedDailyRates,
  formatExchangeRate,
  getCurrencyPairDescription,
  currencyMap,
  currencyNameMap
} from '../utils/getExchangeRate';

// 源货币和目标货币选择
const fromCurrency = ref(1); // 默认人民币
const toCurrency = ref(2);   // 默认美元

// 设置返回结果和加载状态
const apiResult = ref<any>(null);
const processedResult = ref<any>(null);
const loading = ref(false);
const error = ref('');

// 定义可选货币列表
const currencies = reactive(
  Object.entries(currencyMap).map(([key, code]) => ({
    value: parseInt(key),
    label: `${currencyNameMap[code]} (${code})`
  }))
);

// 获取实时汇率
async function fetchRealTimeRate() {
  loading.value = true;
  error.value = '';
  apiResult.value = null;
  processedResult.value = null;
  
  try {
    const data = await getRealTimeExchangeRate(fromCurrency.value, toCurrency.value);
    apiResult.value = data;
    
    // 处理显示数据
    if (data && data['Realtime Currency Exchange Rate']) {
      const rateInfo = data['Realtime Currency Exchange Rate'];
      processedResult.value = {
        fromCurrency: rateInfo['1. From_Currency Code'],
        toCurrency: rateInfo['3. To_Currency Code'],
        rate: formatExchangeRate(rateInfo['5. Exchange Rate']),
        lastUpdated: rateInfo['6. Last Refreshed'],
        description: getCurrencyPairDescription(fromCurrency.value, toCurrency.value)
      };
    }
  } catch (err: any) {
    error.value = err.message || '获取汇率失败';
    console.error(err);
  } finally {
    loading.value = false;
  }
}

// 获取每日汇率数据
async function fetchDailyRates() {
  loading.value = true;
  error.value = '';
  apiResult.value = null;
  processedResult.value = null;
  
  try {
    const data = await getDailyExchangeRate(fromCurrency.value, toCurrency.value);
    apiResult.value = data;
    
    // 处理图表数据 - 获取最近10天数据
    const chartData = await getProcessedDailyRates(fromCurrency.value, toCurrency.value, 10);
    processedResult.value = {
      fromCurrency: data['Meta Data']['2. From Symbol'],
      toCurrency: data['Meta Data']['3. To Symbol'],
      lastUpdated: data['Meta Data']['4. Last Refreshed'],
      recentRates: chartData,
      description: getCurrencyPairDescription(fromCurrency.value, toCurrency.value)
    };
  } catch (err: any) {
    error.value = err.message || '获取每日汇率数据失败';
    console.error(err);
  } finally {
    loading.value = false;
  }
}

// 组件挂载时自动获取一次实时汇率
onMounted(() => {
  fetchRealTimeRate();
});
</script>

<template>
  <div class="exchange-rate-container">
    <h2>汇率查询</h2>
    
    <!-- 查询控制区域 -->
    <div class="query-controls">
      <div class="currency-selectors">
        <div class="selector-group">
          <label>源货币:</label>
          <select v-model="fromCurrency">
            <option v-for="currency in currencies" :key="currency.value" :value="currency.value">
              {{ currency.label }}
            </option>
          </select>
        </div>
        
        <div class="selector-group">
          <label>目标货币:</label>
          <select v-model="toCurrency">
            <option v-for="currency in currencies" :key="currency.value" :value="currency.value">
              {{ currency.label }}
            </option>
          </select>
        </div>
      </div>
      
      <div class="action-buttons">
        <button @click="fetchRealTimeRate" :disabled="loading">获取实时汇率</button>
        <button @click="fetchDailyRates" :disabled="loading">获取每日汇率</button>
      </div>
    </div>
    
    <!-- 加载状态 -->
    <div v-if="loading" class="loading-indicator">
      加载中...
    </div>
    
    <!-- 错误消息 -->
    <div v-if="error" class="error-message">
      {{ error }}
    </div>
    
    <!-- 处理后的结果 -->
    <div v-if="processedResult && !loading" class="processed-result">
      <h3>{{ processedResult.description }}</h3>
      
      <!-- 实时汇率结果 -->
      <div v-if="processedResult.rate" class="rate-display">
        <p class="rate-value">当前汇率: <span>{{ processedResult.rate }}</span></p>
        <p class="last-updated">更新时间: {{ processedResult.lastUpdated }}</p>
      </div>
      
      <!-- 每日汇率结果 -->
      <div v-if="processedResult.recentRates" class="daily-rates">
        <h4>最近10天汇率数据</h4>
        <table>
          <thead>
            <tr>
              <th>日期</th>
              <th>汇率</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(item, index) in processedResult.recentRates" :key="index">
              <td>{{ item.date }}</td>
              <td>{{ formatExchangeRate(item.rate) }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
    
    <!-- 原始JSON结果 -->
    <div v-if="apiResult && !loading" class="raw-result">
      <h3>API返回的原始JSON数据</h3>
      <pre>{{ JSON.stringify(apiResult, null, 2) }}</pre>
    </div>
  </div>
</template>

<style scoped>
.exchange-rate-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  font-family: Arial, sans-serif;
}

h2 {
  color: #333;
  margin-bottom: 20px;
}

.query-controls {
  display: flex;
  flex-direction: column;
  gap: 15px;
  margin-bottom: 20px;
  padding: 15px;
  background-color: #f5f5f5;
  border-radius: 5px;
}

.currency-selectors {
  display: flex;
  gap: 20px;
}

.selector-group {
  display: flex;
  flex-direction: column;
  flex: 1;
}

.selector-group label {
  margin-bottom: 5px;
  font-weight: bold;
}

select {
  padding: 8px;
  border-radius: 4px;
  border: 1px solid #ccc;
}

.action-buttons {
  display: flex;
  gap: 10px;
}

button {
  padding: 10px 15px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background-color: #45a049;
}

button:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}

.loading-indicator {
  text-align: center;
  padding: 20px;
  color: #666;
}

.error-message {
  color: #d9534f;
  padding: 10px;
  margin: 10px 0;
  background-color: #f8d7da;
  border-radius: 4px;
}

.processed-result {
  margin-top: 20px;
  padding: 15px;
  background-color: #e7f3eb;
  border-radius: 5px;
}

.rate-display {
  margin: 15px 0;
}

.rate-value {
  font-size: 24px;
  font-weight: bold;
}

.rate-value span {
  color: #28a745;
}

.last-updated {
  color: #6c757d;
  font-size: 14px;
}

.daily-rates {
  margin-top: 20px;
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 10px;
}

th, td {
  padding: 8px;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

th {
  background-color: #f2f2f2;
}

.raw-result {
  margin-top: 30px;
  padding: 15px;
  background-color: #f8f9fa;
  border-radius: 5px;
  overflow: auto;
}

pre {
  margin: 0;
  white-space: pre-wrap;
  font-family: Consolas, monospace;
  font-size: 14px;
  color: #333;
}
</style>