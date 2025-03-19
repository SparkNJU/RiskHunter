<script setup lang="ts">
import { ElMessage } from 'element-plus'
import { Notification, ArrowRight, DataAnalysis } from '@element-plus/icons-vue'
import { ref, onMounted } from 'vue'
import { getNewsListByType, getNewsById } from '../../api/news'
import router from '../../router'

interface NewsItem {
  id: number
  type: string
  title: string
  time: string
  content?: string
}

const loading = ref(false)
const carouselNews = ref<Record<string, NewsItem[]>>({
  government: [],
  market: []
})
const newsLists = ref<Record<string, NewsItem[]>>({
  government: [],
  market: []
})

const processNewsItem = (type: string, item: any): NewsItem => ({
  id: item.id,
  type,
  title: item.标题 || '无标题',
  time: item.发布时间 || '未知时间',
})

const loadData = async (type: string) => {
  try {
    const res = await getNewsListByType(type)
    const topNews = res.slice(0, 20).map((item: any) => processNewsItem(type, item))

    carouselNews.value[type] = await Promise.all(
      topNews.slice(0, 3).map(async (item: { id: number }) => {
        const res = await getNewsById(type, item.id)
        return {
          ...item,
          content: res.content?.replace(/<h1>.*?<\/h1>/gi, '')
            ?.replace(/<p>\d{4}-\d{2}-\d{2}<\/p>/gi, '')
            ?.trim() || ''
        }
      })
    )
    newsLists.value[type] = topNews.slice(3)
  } catch (error) {
    ElMessage.error(`${type === 'government' ? '政事要闻' : '市场动态'}加载失败`)
    router.push('/')
  }
}

onMounted(async () => {
  try {
    loading.value = true
    await Promise.all([loadData('government'), loadData('market')])
  } finally {
    loading.value = false
  }
})
</script>

<template>
  <el-main class="news-container">
    <el-row :gutter="20" v-loading="loading">
      <!-- 政事要闻版块 -->
      <el-col :xs="24" :sm="24" :md="12">
        <div class="section-header">
          <h3 class="section-title">
            <el-icon>
              <Notification />
            </el-icon>
            政事要闻
          </h3>
          <el-divider />
        </div>

        <el-carousel height="320px" trigger="hover" class="custom-carousel">
          <el-carousel-item v-for="news in carouselNews.government" :key="news.id">
            <el-card class="carousel-card">
              <div class="card-content">
                <h2 class="carousel-title">{{ news.title }}</h2>
                <div class="time">{{ news.time }}</div>
                <div class="carousel-content" v-html="news.content?.substring(0, 120) + '...'"></div>
                <el-button type="primary" size="small" @click="$router.push(`/news/${news.type}/${news.id}`)"
                  class="read-more">
                  阅读全文 <el-icon>
                    <ArrowRight />
                  </el-icon>
                </el-button>
              </div>
            </el-card>
          </el-carousel-item>
        </el-carousel>

        <el-row :gutter="16" class="news-sub-list">
          <el-col v-for="news in newsLists.government" :key="news.id" :span="24">
            <el-card class="news-card" @click="$router.push(`/news/${news.type}/${news.id}`)" shadow="hover">
              <div class="card-body">
                <h3 class="news-title">{{ news.title }}</h3>
                <div class="time">{{ news.time }}</div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </el-col>

      <!-- 市场动态版块 -->
      <el-col :xs="24" :sm="24" :md="12">
        <div class="section-header">
          <h3 class="section-title">
            <el-icon>
              <DataAnalysis />
            </el-icon>
            市场动态
          </h3>
          <el-divider />
        </div>

        <el-carousel height="320px" trigger="hover" class="custom-carousel">
          <el-carousel-item v-for="news in carouselNews.market" :key="news.id">
            <el-card class="carousel-card">
              <div class="card-content">
                <h2 class="carousel-title">{{ news.title }}</h2>
                <div class="time">{{ news.time }}</div>
                <div class="carousel-content" v-html="news.content?.substring(0, 120) + '...'"></div>
                <el-button type="primary" size="small" @click="$router.push(`/news/${news.type}/${news.id}`)"
                  class="read-more">
                  阅读全文 <el-icon>
                    <ArrowRight />
                  </el-icon>
                </el-button>
              </div>
            </el-card>
          </el-carousel-item>
        </el-carousel>

        <el-row :gutter="16" class="news-sub-list">
          <el-col v-for="news in newsLists.market" :key="news.id" :span="24">
            <el-card class="news-card" @click="$router.push(`/news/${news.type}/${news.id}`)" shadow="hover">
              <div class="card-body">
                <h3 class="news-title">{{ news.title }}</h3>
                <div class="time">{{ news.time }}</div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </el-col>
    </el-row>
  </el-main>
</template>

<style scoped>
.news-container {
  padding: 20px;
  max-width: 1600px;
}

.section-column {
  padding: 0 20px;
}

.section-header {
  margin-bottom: 25px;
  
  .section-title {
    display: flex;
    align-items: center;
    gap: 8px;
    color: var(--el-color-primary);
    font-size: 1.4rem;
    margin: 0 0 12px;
    
    .el-icon {
      font-size: 1.6rem;
    }
  }
  
  .el-divider {
    margin: 0;
    border-color: var(--el-border-color-extra-light);
  }
}

.custom-carousel {
  border-radius: 8px;
  overflow: hidden;
  box-shadow: var(--el-box-shadow-light);
  
  :deep(.el-carousel__indicators) {
    padding-bottom: 10px;
  }
}

.carousel-card {
  height: 320px;
  display: flex;
  border: none !important;
  
  :deep(.el-card__body) {
    flex: 1;
    padding: 25px;
    display: flex;
    flex-direction: column;
  }

  .carousel-title {
    color: var(--el-color-primary);
    font-size: 1.6rem;
    line-height: 1.4;
    margin: 0 0 15px;
  }

  .carousel-content {
    flex: 1;
    color: var(--el-text-color-regular);
    line-height: 1.6;
    overflow: hidden;
    display: -webkit-box;
    margin-bottom: 15px; /* 保持与按钮的间距 */
  }

  .card-content {
    flex: 1;
    display: flex;
    flex-direction: column;
    padding-bottom: 60px;
  }

  .read-more {
    position: absolute;
    left: 25px;
    bottom: 25px;
    margin-top: 0;
  }
}

.news-sub-list {
  margin: 30px 0;
  
  .el-col {
    margin-bottom: 16px;
    
    &:last-child {
      margin-bottom: 0;
    }
  }
}

.news-card {
  height: 120px;
  transition: transform 0.3s var(--el-transition-function-fast-bezier);
  
  :deep(.el-card__body) {
    padding: 18px;
    height: 100%;
  }

  .card-body {
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
  }

  .news-title {
    font-size: 1.1rem;
    line-height: 1.4;
    margin: 0;
    color: var(--el-text-color-primary);
    display: -webkit-box;
    overflow: hidden;
  }

  &:hover {
    transform: translateY(-5px);
    box-shadow: var(--el-box-shadow) !important;
  }
}

.time {
  color: var(--el-text-color-secondary);
  font-size: 0.85rem;
  margin-top: 8px;
}

@media (max-width: 768px) {
  .section-column {
    padding: 0;
    margin-bottom: 30px;
    
    &:last-child {
      margin-bottom: 0;
    }
  }
  
  .carousel-card {
    height: 360px;
    
    .carousel-title {
      font-size: 1.4rem;
    }
  }
}
</style>