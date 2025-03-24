<template>
  <el-container class="dashboard-container">
    <el-row class="top-section" :gutter="20">
      <el-col :xs="24" :sm="8">
        <el-card class="gauge-card">
          <Gauge :risk-index="riskIndex" />
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="8">
        <el-card class="indicator-card">
          <h2>指标展示</h2>
          <el-table :data="indicatorData" style="width: 100%">
            <el-table-column prop="name" label="指标" />
            <el-table-column prop="currentValue" label="当前值" />
            <el-table-column prop="trend" label="预测走势" />
          </el-table>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="8">
        <el-card class="warning-list-card">
          <h2>主要预警列表</h2>
          <ul>
            <li>预警1：...</li>
            <li>预警2：...</li>
            <li>预警3：...</li>
          </ul>
        </el-card>
      </el-col>
    </el-row>

    <el-row class="advice-section">
      <el-col :span="24">
        <el-card class="advice-card">
          <template #header>
            <div class="advice-header">
              <div class="advice-header-left">
                <el-icon class="adivce-header-icon">
                  <Money />
                </el-icon>
                <span class="advice-header-title">投资建议</span>
              </div>
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

          <div class="advice-content">
            <div class="advice-title">{{ currentAdvice.title }}</div>
            <p>{{ currentAdvice.description }}</p>
            <ul>
              <li v-for="(item, index) in currentAdvice.items" :key="index">{{ item }}</li>
            </ul>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </el-container>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue';
import { Money } from '@element-plus/icons-vue';
// 模拟风险指数
const riskIndex = ref(65);

// 投资建议类型选择
const selectedAdviceType = ref<'aggressive' | 'balanced' | 'conservative'>('balanced');

// 投资建议配置
const adviceContents = ref({
  aggressive: {
    title: '激进型投资建议',
    description: '适合风险承受能力强的投资者：',
    items: [
      '建议1：投资于高波动性货币对',
      '建议2：采用短期交易策略', 
      '建议3：考虑杠杆交易但需控制风险'
    ]
  },
  balanced: {
    title: '平衡型投资建议',
    description: '适合风险承受能力中等的投资者：',
    items: [
      '建议1：投资于中等波动性货币对',
      '建议2：采用中期交易策略',
      '建议3：多样化投资组合分散风险'
    ]
  },
  conservative: {
    title: '保守型投资建议',
    description: '适合风险承受能力弱的投资者：',
    items: [
      '建议1：投资于低波动性货币对',
      '建议2：采用长期交易策略',
      '建议3：考虑对冲策略降低风险'
    ]
  }
});

// 当前建议内容
const currentAdvice = computed(() => 
  adviceContents.value[selectedAdviceType.value]
);

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
  /* 宽度一致 */
  width: 75vw;
}

.top-section {
  margin-bottom: 1rem;
}

.gauge-card,
.indicator-card,
.warning-list-card {
  height: 100%;
}

.advice-card {
  width: 100%;
  margin-bottom: 1rem;
}

.advice-header {
  height: 2rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.advice-header-left {
  display: flex;
  align-items: center;
  gap: 8px;
}

.advice-header-title {
  font-size: 1.1em;
  font-weight: 600;
  color: var(--el-text-color-primary);
}

.adivce-header-icon {
  font-size: 1.1em;
  color: var(--el-color-primary);
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
    width: 90vw;
    /* 与移动设备上的 signal-card 保持一致 */
  }

  .top-section {
    margin-bottom: 10px;
  }

  .advice-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
}
</style>