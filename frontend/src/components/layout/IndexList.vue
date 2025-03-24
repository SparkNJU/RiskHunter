<script setup lang="ts">
import { ref, onMounted, onUnmounted } from "vue";
import { Timer } from '@element-plus/icons-vue'
import MarqueeText from 'vue-marquee-text-component'

// 货币列表数据 - 模拟实时汇率
const currencyList = ref([
  { number: 1, code: 'CNY', name: '人民币', rate: 7.0923, change: -0.03 },
  //{ number: 2, code: 'USD', name: '美元', rate: 1.0000, change: 0.00 },
  { number: 3, code: 'EUR', name: '欧元', rate: 0.9132, change: 0.05 },
  { number: 4, code: 'JPY', name: '日元', rate: 154.39, change: -0.21 },
  { number: 5, code: 'GBP', name: '英镑', rate: 0.7824, change: 0.02 },
  { number: 6, code: 'AUD', name: '澳元', rate: 1.4873, change: -0.08 },
  { number: 7, code: 'HKD', name: '港币', rate: 7.8103, change: -0.01 },
  { number: 8, code: 'CHF', name: '瑞士法郎', rate: 0.9054, change: 0.03 }
]);

// 是否暂停滚动
const isHover = ref(false);

// 当前时间
const currentTime = ref('');
let timer: number | null = null;

// 更新当前时间的函数
const updateTime = () => {
  const now = new Date();
  const year = now.getFullYear();
  const month = String(now.getMonth() + 1).padStart(2, '0');
  const day = String(now.getDate()).padStart(2, '0');
  const hours = String(now.getHours()).padStart(2, '0');
  const minutes = String(now.getMinutes()).padStart(2, '0');
  const seconds = String(now.getSeconds()).padStart(2, '0');
  
  // 格式化为 YYYY-MM-DD HH:MM:SS
  currentTime.value = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
};

const handleMouseover = (): void => {
  isHover.value = true;
};

const handleMouseout = () => {
  isHover.value = false;
};

// 组件挂载时启动时间更新
onMounted(() => {
  // 立即更新一次
  updateTime();
  // 设置每秒更新一次
  timer = window.setInterval(updateTime, 1000);
});

// 组件卸载时清除定时器
onUnmounted(() => {
  if (timer !== null) {
    clearInterval(timer);
    timer = null;
  }
});
</script>

<template>
  <el-affix :offset="56">
    <div class="currency-ticker">
      <div class="ticker-title">
        <el-icon><Timer /></el-icon>
        <span>{{ currentTime }}</span>
      </div>
      <div class="ticker-content">
        <marquee-text :repeat="5" :duration="60" :paused="isHover" @mouseover="handleMouseover" @mouseout="handleMouseout">
          <div class="currency-item" v-for="(item, index) in currencyList" :key="index">
            <span class="currency-name">{{ item.name }}</span>
            <span class="currency-code">{{ item.code }}/USD</span>
            <span class="currency-rate">{{ item.rate.toFixed(4) }}</span>
            <span class="currency-change" :class="{ 'up': item.change > 0, 'down': item.change < 0, 'neutral': item.change === 0 }">
              {{ item.change > 0 ? '+' : '' }}{{ item.change.toFixed(2) }}%
            </span>
          </div>
        </marquee-text>
      </div>
    </div>
  </el-affix>
</template>

<style scoped>
.currency-ticker {
  display: flex;
  align-items: center;
  background: var(--el-bg-color);
  border-bottom: 1px solid var(--el-border-color-lighter);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  width: 100%;
  height: 40px;
  z-index: 999;
}

.ticker-title {
  display: flex;
  align-items: center;
  padding: 0 16px;
  font-weight: 500;
  width: 174px;
  color: #ffffff;
  background-color: #6a56c6a2;
  border-right: 1px solid var(--el-border-color-lighter);
  height: 100%;
  font-size: 15px;
  min-width: 174px; /* 增加宽度以适应时间格式 */
}

.ticker-title .el-icon {
  margin-right: 8px;
  color: #ffffff;
  font-size: 18px;
}

/* 其余样式保持不变 */
.ticker-content {
  flex: 1;
  height: 100%;
  overflow: hidden;
}

.currency-item {
  display: inline-flex;
  align-items: center;
  padding: 0 12px;
  margin: 0 8px;
  height: 40px;
  border-right: 1px solid var(--el-border-color-lighter);
}

.currency-item:last-child {
  border-right: none;
}

.currency-name {
  font-size: 14px;
  color: var(--el-text-color-primary);
  margin-right: 8px;
}

.currency-code {
  font-weight: 500;
  margin-right: 10px;
  color: var(--el-text-color-primary);
}

.currency-rate {
  margin-right: 10px;
  font-family: 'Courier New', monospace;
  font-weight: 800;
}

.currency-change {
  font-weight: 500;
  font-family: 'Courier New', monospace;
  padding: 2px 6px;
  border-radius: 3px;
}

.up {
  color: #67c23a;
  background-color: rgba(103, 194, 58, 0.1);
}

.down {
  color: #f56c6c;
  background-color: rgba(245, 108, 108, 0.1);
}

.neutral {
  color: #909399;
  background-color: rgba(144, 147, 153, 0.1);
}
</style>