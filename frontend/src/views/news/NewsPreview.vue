<script setup lang="ts">
import { ElMessage } from 'element-plus'
import { ref } from 'vue'
import { getAllNews, getNewsById } from '../../api/news'

interface NewsItem {
  id: number
  title: string
  time: string
  content?: string
}

// const router = useRouter()
const loading = ref(false)

// 走马灯新闻
const carouselNews = ref<NewsItem[]>([])
// 新闻列表
const newsList = ref<NewsItem[]>([])

const processNewsItem = (item: any): NewsItem => ({
  id: item.id,
  title: item.标题 || '无标题',
  time: item.发布时间 || '未知时间',
})

const loadData = async () => {
  try {
    loading.value = true
    const res = await getAllNews()
    const topNews = res.slice(0, 20).map(processNewsItem)
    carouselNews.value = await Promise.all(
      topNews.slice(0, 4).map(async (item: { id: number }) => {
        const res = await getNewsById(item.id)
        return {
          ...item,
          content: res.content?.replace(/<h1>.*?<\/h1>/gi, '')
                     ?.replace(/<p>\d{4}-\d{2}-\d{2}<\/p>/gi, '')
                     ?.trim() || ''
        }
      })
    )
    newsList.value = topNews.slice(4)
    
  } catch (error) {
    ElMessage.error('新闻加载失败')
    console.error('新闻加载失败:', error)
  } finally {
    loading.value = false
  }
}
loadData()
</script>

<template>
  <el-main class="news-container">
    <el-row :gutter="20" v-loading="loading">
      <!-- 走马灯区域 -->
      <el-col :xs="24" :sm="24" :md="12">
        <el-carousel>
          <el-carousel-item v-for="news in carouselNews" :key="news.id" class="full-height-item">
            <el-card class="carousel-card">
              <h2>{{ news.title }}</h2>
              <div class="time">{{ news.time }}</div>
              <div class="carousel-content" v-html="news.content?.substring(0, 100) + '...'"></div>
              <el-link type="primary" @click="$router.push(`/news/${news.id}`)">阅读全文</el-link>
            </el-card>
          </el-carousel-item>
        </el-carousel>
      </el-col>


      <!-- 新闻列表: 右上 -->
      <el-col :xs="24" :sm="24" :md="12">
        <el-row :gutter="20">
          <el-col v-for="news in newsList.slice(0, 3)" :key="news.id" :span="24">
            <el-card @click="$router.push(`/news/${news.id}`)" class="news-card">
              <h3>{{ news.title }}</h3>
              <div class="time">{{ news.time }}</div>
            </el-card>
          </el-col>
        </el-row>
      </el-col>

      <!-- 新闻列表: 下方 -->
      <el-col :xs="24" :sm="24" :md="24">
        <el-row :gutter="20">
          <el-col v-for="news in newsList.slice(3)" :key="news.id" :xs="24" :sm="24" :md="12">
            <el-card @click="$router.push(`/news/${news.id}`)" class="news-card">
              <h3>{{ news.title }}</h3>
              <div class="time">{{ news.time }}</div>
            </el-card>
          </el-col>
        </el-row>
      </el-col>
    </el-row>
  </el-main>
</template>

<style scoped>
:deep(.el-carousel) {
  h2 {
    color: var(--el-text-color-primary);
    font-size: 1.5rem;
    margin-bottom: 12px;
  }
}

.carousel-card {
  height: 100%;
  display: flex;
  flex-direction: column;
  
  :deep(.el-card__body) {
    flex: 1;
    display: flex;
    flex-direction: column;
  }
  
  .carousel-content {
    flex: 1;
    overflow: hidden;
  }
}

.news-card {
  min-height: 120px;
  margin-bottom: 1rem;
  transition: all 0.3s var(--el-transition-function-fast-bezier);

  &:hover {
    transform: translateY(-3px);
    box-shadow: var(--el-box-shadow-dark);
    border-color: var(--el-color-primary);
  }

  h3 {
    color: var(--el-text-color-primary);
    font-size: 1.1rem;
    margin-bottom: 0.5rem;
    overflow: hidden;
  }
}

.time {
  color: var(--el-text-color-secondary);
  font-size: 0.85rem;
}
</style>