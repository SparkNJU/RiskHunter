<script setup lang="ts">
import { DataLine } from '@element-plus/icons-vue'
</script>

<template>
  <el-main class="main-container">
    <el-card class="info-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <el-icon class="header-icon"><DataLine /></el-icon>
          <span class="title">外汇数据</span>
        </div>
      </template>
      
      <el-empty description="外汇数据展示页面正在开发中..." />
    </el-card>
  </el-main>
</template>

<style scoped>
/* 主布局样式 */
.main-container {
  min-height: 100vh;
  background: linear-gradient(135deg, var(--el-color-primary-light-9) 0%, var(--el-color-primary-light-5) 100%);
  display: flex;
  flex-direction: column;
  padding: 24px;
  align-items: center;
}

/* 卡片基础样式 */
.info-card {
  width: 800px;
  max-width: 100%;
  transition: all 0.3s ease;
  border-radius: 12px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
  background-color: var(--el-bg-color);
}

.info-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.15);
}

/* 卡片头部样式 */
.card-header {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 4px 0;
}

.header-icon {
  font-size: 20px;
  color: var(--el-color-primary);
}

.title {
  font-size: 20px;
  font-weight: bold;
  color: var(--el-color-primary);
}

/* 响应式设计 */
@media (max-width: 840px) {
  .main-container {
    padding: 12px;
  }

  .info-card {
    width: 100%;
  }
}

/* 暗色主题适配 */
:deep(.dark) {
  .main-container {
    background: linear-gradient(135deg, 
      var(--el-color-primary-dark-9) 0%, 
      var(--el-color-primary-dark-5) 100%
    );
  }

  .info-card {
    background-color: var(--el-bg-color-overlay);
  }
}
</style>