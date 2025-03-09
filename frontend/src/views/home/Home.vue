<script setup lang="ts">
import { InfoFilled, Warning, Memo, DataLine, Phone, Message, Location, Service } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const navigateTo = (path: string) => {
  router.push(path)
}
const funcs = [
  { icon: Warning, title: '风险信号', path: '/risk-signal' },
  { icon: Service, title: '智能助手', path: '/chat' },
  { icon: DataLine, title: '外汇数据', path: '/forex-data' },
  { icon: Memo, title: '新闻展示', path: '/news' }
]
</script>

<template>
  <el-main class="home-container">
    <!-- 介绍栏 -->
    <div class="intro">
      <div class="intro-header">
        <h1 class="intro-title">Risk Hunter</h1>
        <p class="intro-subtitle">您身边的外汇风险管理助手</p>
      </div>
      <div class="intro-about">
        <el-icon>
          <InfoFilled />
        </el-icon>
        <span>关于我们</span>
      </div>
      <p class="intro-description">
        Risk Hunter 是一个前沿的统计与AI工具，能够整合分析过往趋势，通过多维度的数据模型精准捕捉汇率风险信号，为企业提供更具前瞻性、科学性的风险管理支持。
      </p>
    </div>

    <!-- 功能栏 -->
    <div class="function-grid">
      <el-card v-for="(item, index) in funcs" :key="index" class="function-card">
        <div class="function-icon">
          <el-icon>
            <component :is="item.icon" />
          </el-icon>
        </div>
        <h3 class="card-title">{{ item.title }}</h3>
        <el-button type="primary" @click="navigateTo(item.path)" class="card-button">
          查看详情
        </el-button>
      </el-card>
    </div>

    <!-- 联系栏 -->
    <div class="contact-container">
      <div class="contact-header">
        <el-icon>
          <Phone />
        </el-icon>
        <h3 class="contact-title">联系我们</h3>
      </div>

      <div class="contact-content">
        <div class="contact-item">
          <el-icon class="contact-icon">
            <Message />
          </el-icon>
          <span>邮箱：</span>
          <a href="mailto:riskhunter@smail.nju.edu.cn">
            riskhunter@smail.nju.edu.cn
          </a>
        </div>

        <div class="contact-item">
          <el-icon class="contact-icon">
            <Location />
          </el-icon>
          <span>地址：中国江苏省南京市栖霞区南京大学</span>
        </div>
      </div>
    </div>
  </el-main>
</template>

<style scoped>
/* 介绍栏 */
.home-container {
  background: linear-gradient(135deg, #e0f7fa 0%, #e1bee7 100%);
  padding: 0; /* 移除默认内边距 */
}

/* 使el-main能够铺满宽度 */
:deep(.el-main) {
  padding: 0;
}

.intro {
  margin-bottom: 2rem;
  text-align: center;
  padding: 2rem 1rem;
}

.intro-header {
  margin-bottom: 1.5rem;
}

.intro-title {
  color: var(--el-color-primary);
  font-size: 2.5rem;
  margin-bottom: 0.5rem;
  line-height: 1.2;
}

.intro-subtitle {
  color: var(--el-text-color-secondary);
  font-size: 1.2rem;
}

.intro-about {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  margin: 1.5rem 0;
  color: var(--el-color-primary);
}

.intro-description {
  color: var(--el-text-color-regular);
  line-height: 1.8;
  max-width: 800px;
  margin: 0 auto;
  text-align: justify;
}

/* 功能栏 */
.function-grid {
  display: grid;
  grid-template-columns: 1fr; /* 修改为单列布局 */
  gap: 0; /* 在卡片之间保留间距 */
  padding: 0; /* 移除内边距让卡片宽度铺满屏幕 */
  margin: 0; /* 移除外边距 */
  width: 100%;
}

.function-card {
  padding: 1.5rem;
  border: 1px solid var(--el-border-color-light);
  transition: all 0.3s var(--el-transition-function-fast-bezier);
  background: linear-gradient(to right, #dbeafe, #f3e5f5);
  display: flex;
  flex-direction: column;
  align-items: center;
  border-radius: 0; /* 移除圆角让卡片完全铺满宽度 */
  width: 100%;
  height:500px;
  margin: 0; /* 移除外边距 */
}

.function-card:hover {
  transform: translateY(-5px);
  box-shadow: var(--el-box-shadow-light);
}

.function-icon {
  display: flex;
  justify-content: center;
  font-size: 2.5rem;
  margin-bottom: 1.2rem;
  color: var(--el-color-primary);
}

.card-title {
  margin-bottom: 1.5rem;
  font-size: 1.5rem;
  color: var(--el-text-color-primary);
}

.card-button {
  width: 100%;
  margin-top: auto;
}

/* 联系栏 */
.contact-container {
  padding: 2rem 1rem;
  margin: 0; /* 移除外边距 */
  width: 100%; /* 使容器占满宽度 */
  max-width: 100%; /* 移除宽度限制 */
}

.contact-header {
  display: flex;
  align-items: center;
  gap: 0.8rem;
  margin-bottom: 1.5rem;
  justify-content: center;
}

.contact-title {
  color: var(--el-text-color-primary);
  margin: 0;
  font-size: 1.4rem;
}

.contact-content {
  max-width: 800px;
  margin: 0 auto;
}

.contact-item {
  display: flex;
  align-items: center;
  gap: 0.8rem;
  margin-bottom: 1rem;
  justify-content: center;
}

.contact-icon {
  font-size: 1.2rem;
}
</style>