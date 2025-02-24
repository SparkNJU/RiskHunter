<script setup lang="ts">
import { InfoFilled, Operation, Phone, Location, Message } from '@element-plus/icons-vue'
</script>

<template>
  <el-main class="main-container">
    <el-card class="info-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <span class="title">Risk Hunter</span>
          <div class="title-divider"></div>
          <span class="subtitle">这里是副标题</span>
        </div>
      </template>

      <!-- TODO: 简介卡片 -->
      <el-row :gutter="20">
        <el-col :span="24">
          <el-card class="inner-card" shadow="hover">
            <template #header>
              <div class="section-header">
                <el-icon class="section-icon"><InfoFilled /></el-icon>
                <span>关于我们</span>
              </div>
            </template>
            <p class="section-content">
              Risk Hunter 是一个前沿的统计与AI工具，能够整合分析过往趋势，通过多维度的数据模型精准捕捉汇率风险信号，为企业和金融机构提供更具前瞻性、科学性的风险管理支持。
            </p>
          </el-card>
        </el-col>
      </el-row>

      <!-- TODO: 功能卡片 -->
      <el-row :gutter="20" class="mt-20">
        <el-col :span="24">
          <el-card class="inner-card feature-card" shadow="hover">
            <template #header>
              <div class="section-header">
                <el-icon class="section-icon"><Operation /></el-icon>
                <span>核心功能</span>
              </div>
            </template>
            <el-descriptions direction="vertical" :column="1" border>
              <el-descriptions-item class="feature-item">
                <template #label>
                  <div class="feature-label">单一货币对分析</div>
                </template>
                分析不同因素对汇率的影响，形成单一货币对的风险信号，展示回测效果。
              </el-descriptions-item>
              <el-descriptions-item class="feature-item">
                <template #label>
                  <div class="feature-label">多货币智能分析</div>
                </template>
                针对多货币敞口的情况，根据各货币对的相关性，形成多货币对的风险信号智能风险分析。
              </el-descriptions-item>
              <el-descriptions-item class="feature-item">
                <template #label>
                  <div class="feature-label">定制化风险预警</div>
                </template>
                根据对最大回撤比例、交易期限的定制化需求微调信号参数。风险预警系统。
              </el-descriptions-item>
            </el-descriptions>
          </el-card>
        </el-col>
      </el-row>

      <!-- TODO: 联系卡片 -->
      <el-row :gutter="20" class="mt-20">
        <el-col :span="24">
          <el-card class="inner-card contact-card" shadow="hover">
            <template #header>
              <div class="section-header">
                <el-icon class="section-icon"><Phone /></el-icon>
                <span>联系我们</span>
              </div>
            </template>
            <el-descriptions :column="1" border>
              <el-descriptions-item>
                <template #label>
                  <div class="label-with-icon">
                    <el-icon class="contact-icon"><Message /></el-icon>
                    <span>邮箱</span>
                  </div>
                </template>
                <a href="mailto:riskhunter@smail.nju.edu.cn" class="contact-link">
                  riskhunter@smail.nju.edu.cn
                </a>
              </el-descriptions-item>
              <el-descriptions-item>
                <template #label>
                  <div class="label-with-icon">
                    <el-icon class="contact-icon"><Location /></el-icon>
                    <span>地址</span>
                  </div>
                </template>
                <span class="address-text">中国江苏省南京市栖霞区南京大学</span>
              </el-descriptions-item>
            </el-descriptions>
          </el-card>
        </el-col>
      </el-row>
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

/* 标题样式 */
.card-header {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px 0;
}

.title {
  font-size: 32px;
  font-weight: bold;
  color: var(--el-color-primary);
  margin-bottom: 12px;
}

.title-divider {
  width: 60px;
  height: 3px;
  background: var(--el-color-primary);
  margin: 12px 0;
}

.subtitle {
  font-size: 16px;
  color: var(--el-text-color-secondary);
}

/* 卡片通用样式 */
.inner-card {
  margin-bottom: 20px;
  transition: all 0.3s ease;
  border-radius: 8px;
}

.section-header {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 4px 0;
}

.section-icon {
  font-size: 20px;
  color: var(--el-color-primary);
}

.section-content {
  line-height: 1.8;
  color: var(--el-text-color-regular);
  padding: 16px;
  text-align: justify;
}

/* 功能卡片样式 */
.feature-card :deep(.el-descriptions__cell) {
  padding: 20px;
}

.feature-label {
  font-size: 16px;
  font-weight: 600;
  color: var(--el-color-primary);
}

/* 联系卡片样式 */
.contact-card .label-with-icon {
  display: flex;
  align-items: center;
  gap: 8px;
}

.contact-icon {
  font-size: 16px;
  color: var(--el-color-primary);
}

.contact-link {
  color: var(--el-color-primary);
  text-decoration: none;
  transition: color 0.3s ease;
}

.contact-link:hover {
  color: var(--el-color-primary-light-3);
}

.address-text {
  color: var(--el-text-color-regular);
}

/* Element Plus 组件深度样式 */
:deep(.el-descriptions) {
  --el-descriptions-item-bordered-label-background: var(--el-color-primary-light-9);
}

:deep(.el-descriptions__label) {
  width: 120px;
  justify-content: flex-start;
  padding: 12px 20px;
}

:deep(.el-descriptions__content) {
  padding: 12px 20px;
}

/* 通用工具类 */
.mt-20 {
  margin-top: 20px;
}

/* 悬停效果 */
.inner-card:hover {
  transform: translateY(-2px);
  box-shadow: var(--el-box-shadow-light);
}

/* 响应式设计 */
@media (max-width: 840px) {
  .main-container {
    padding: 12px;
  }

  .info-card {
    width: 100%;
  }

  .title {
    font-size: 24px;
  }

  .subtitle {
    font-size: 14px;
  }

  .section-content {
    padding: 12px;
  }

  .contact-card .label-with-icon {
    gap: 6px;
  }
}

/* 暗色主题适配 */
:deep(.dark) {
  .title-divider {
    background: var(--el-color-primary-light-3);
  }
  
  .main-container {
    background: linear-gradient(135deg, 
      var(--el-color-primary-dark-9) 0%, 
      var(--el-color-primary-dark-5) 100%
    );
  }

  .info-card,
  .inner-card {
    background-color: var(--el-bg-color-overlay);
  }
}
</style>