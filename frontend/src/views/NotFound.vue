<script setup lang="ts">
import { Warning } from '@element-plus/icons-vue'
</script>


<template>
  <el-main class="main-frame">
    <el-card class="error-card" shadow="hover">
      <el-result
        icon="warning"
        title="404"
        sub-title="抱歉，您访问的页面不存在"
      >
        <template #icon>
          <el-icon class="error-icon"><Warning /></el-icon>
        </template>
        <template #extra>
          <router-link to="/about">
            <el-button type="primary" class="back-btn">返回主页</el-button>
          </router-link>
        </template>
      </el-result>
    </el-card>
  </el-main>
</template>


<style scoped>
/* 主布局样式 */
.main-frame {
  min-height: 100vh;
  background: linear-gradient(135deg, var(--el-color-primary-light-9) 0%, var(--el-color-primary-light-5) 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

/* 错误卡片样式 */
.error-card {
  width: 100%;
  max-width: 500px;
  border-radius: 12px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.error-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.15);
}

/* 错误图标样式 */
.error-icon {
  font-size: 84px;
  color: var(--el-color-warning);
}

/* 按钮样式 */
.back-btn {
  padding: 12px 24px;
  font-size: 16px;
  border-radius: 6px;
}

/* Element Plus 组件深度样式 */
:deep(.el-result__title) {
  font-size: 48px;
  color: var(--el-text-color-primary);
  margin-top: 20px;
}

:deep(.el-result__sub-title) {
  font-size: 18px;
  color: var(--el-text-color-secondary);
  margin-top: 10px;
}

/* 响应式设计 */
@media (max-width: 640px) {
  .error-card {
    margin: 0 12px;
  }

  :deep(.el-result__title) {
    font-size: 36px;
  }

  :deep(.el-result__sub-title) {
    font-size: 16px;
  }

  .error-icon {
    font-size: 64px;
  }
}

/* 暗色主题适配 */
:deep(.dark) {
  .main-frame {
    background: linear-gradient(135deg, 
      var(--el-color-primary-dark-9) 0%, 
      var(--el-color-primary-dark-5) 100%
    );
  }
}
</style>
