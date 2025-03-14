<template>
  <div class="dashboard-container">
    <div class="top-section">
      <el-card class="gauge-wrapper">
        <Gauge :risk-index="riskIndex" />
      </el-card>
      <el-card class="indicator-wrapper">
        <h2>指标展示</h2>
        <el-table :data="indicatorData" style="width: 100%">
          <el-table-column prop="name" label="指标" />
          <el-table-column prop="currentValue" label="当前值" />
          <el-table-column prop="trend" label="预测走势" />
        </el-table>
      </el-card>
      <el-card class="warning-list-wrapper">
        <h2>主要预警列表</h2>
        <ul>
          <li>预警1：...</li>
          <li>预警2：...</li>
          <li>预警3：...</li>
        </ul>
      </el-card>
    </div>
    
    <el-card class="advice-card">
      <template #header>
        <div class="advice-header">
          <h2>投资建议</h2>
          <el-radio-group v-model="selectedAdviceType">
            <el-radio :value="'aggressive'">
              <el-tag type="danger" effect="dark" size="small">激进型</el-tag>
            </el-radio>
            <el-radio :value="'balanced'">
              <el-tag type="warning" effect="dark" size="small">平衡型</el-tag>
            </el-radio>
            <el-radio :value="'conservative'">
              <el-tag type="success" effect="dark" size="small">保守型</el-tag>
            </el-radio>
          </el-radio-group>
        </div>
      </template>
      
      <div class="advice-content" v-if="selectedAdviceType === 'aggressive'">
        <div class="advice-title">激进型投资建议</div>
        <p>适合风险承受能力强的投资者：</p>
        <ul>
          <li>建议1：投资于高波动性货币对</li>
          <li>建议2：采用短期交易策略</li>
          <li>建议3：考虑杠杆交易但需控制风险</li>
        </ul>
      </div>
      
      <div class="advice-content" v-if="selectedAdviceType === 'balanced'">
        <div class="advice-title">平衡型投资建议</div>
        <p>适合风险承受能力中等的投资者：</p>
        <ul>
          <li>建议1：投资于中等波动性货币对</li>
          <li>建议2：采用中期交易策略</li>
          <li>建议3：多样化投资组合分散风险</li>
        </ul>
      </div>
      
      <div class="advice-content" v-if="selectedAdviceType === 'conservative'">
        <div class="advice-title">保守型投资建议</div>
        <p>适合风险承受能力弱的投资者：</p>
        <ul>
          <li>建议1：投资于低波动性货币对</li>
          <li>建议2：采用长期交易策略</li>
          <li>建议3：考虑对冲策略降低风险</li>
        </ul>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import Gauge from './Gauge.vue';

// 模拟风险指数
const riskIndex = ref(65);

// 投资建议类型选择
const selectedAdviceType = ref('balanced');

// 模拟指标数据
const indicatorData = ref([
  { name: '汇率波动', currentValue: '3.2%', trend: '上升' },
  { name: '利率差', currentValue: '1.5%', trend: '稳定' },
  { name: '外汇储备', currentValue: '3.1万亿', trend: '下降' },
]);
</script>

<style scoped>
.dashboard-container {
  display: flex;
  flex-direction: column;
  width: 75vw; /* 与 signal-card 保持一致 */
  margin-bottom: 1rem;
}

.top-section {
  display: flex;
  width: 100%;
  gap: 10px;
  margin-bottom: 1rem;
}

.gauge-wrapper,
.indicator-wrapper,
.warning-list-wrapper {
  flex: 1;
  height: auto;
}

.advice-card {
  width: 100%;
  margin-bottom: 1rem;
}

.advice-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.advice-header h2 {
  margin: 0;
  font-size: 1.1rem;
}

.advice-content {
  padding: 15px 10px;
}

.advice-title {
  font-size: 1.1rem;
  font-weight: 600;
  margin-bottom: 10px;
  color: var(--el-color-primary);
}

.advice-content ul {
  padding-left: 20px;
}

.advice-content li {
  margin-bottom: 8px;
}

/* 响应式设计 */
@media screen and (max-width: 768px) {
  .dashboard-container {
    width: 90vw; /* 与移动设备上的 signal-card 保持一致 */
  }
  
  .top-section {
    flex-direction: column;
  }
  
  .advice-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
}
</style>