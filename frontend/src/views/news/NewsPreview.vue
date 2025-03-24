<script setup lang="ts">
import { ElMessage } from 'element-plus'
import { Notification, ArrowRight, DataAnalysis } from '@element-plus/icons-vue'
import { ref, onMounted } from 'vue'
import { getNewsListByType, getNewsById, newsImages } from '../../api/news'
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
    const topNews = res.reverse().slice(0, 20).map((item: any) => processNewsItem(type, item))

    carouselNews.value[type] = await Promise.all(
      topNews.slice(0, 4).map(async (item: { id: number }) => {
        const res = await getNewsById(type, item.id)
        return {
          ...item,
          content: res.content?.replace(/<h1>.*?<\/h1>/gi, '')
            ?.replace(/<p>\d{4}-\d{2}-\d{2}<\/p>/gi, '')
            ?.trim() || ''
        }
      })
    )
    newsLists.value[type] = topNews.slice(4)
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
          <router-link to="/news/government" class="more-link">更多</router-link>
        </div>

        <el-carousel height="480px" trigger="hover" class="custom-carousel">
          <el-carousel-item v-for="news in carouselNews.government" :key="news.id">
            <el-card class="carousel-card">
              <div class="carousel-image">
                <el-image :src="newsImages[news.id % 4]" fit="cover" class="news-image"
                  :preview-src-list="newsImages" />
              </div>
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
          <router-link to="/news/market" class="more-link">更多</router-link>
        </div>

        <el-carousel height="480px" trigger="hover" class="custom-carousel">
          <el-carousel-item v-for="news in carouselNews.market" :key="news.id">
            <el-card class="carousel-card">
              <div class="carousel-image">
                <el-image :src="newsImages[news.id % 4]" fit="cover" class="news-image"
                  :preview-src-list="newsImages" />
              </div>
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
}

.section-column {
  padding: 0 20px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25px;
}

.section-header .section-title {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #6a56c6; /* 更改为紫色 */
  font-size: 1.4rem;
  margin: 0 0 12px;
  font-weight: 600;
}

.section-header .section-title .el-icon {
  font-size: 1.6rem;
  color: #6a56c6; /* 确保图标也是紫色 */
}

/* 增强更多按钮样式 */
.section-header .more-link {
  color: white;
  background-color: #6a56c6;
  padding: 8px 16px;
  border-radius: 4px;
  font-size: 1rem;
  font-weight: 500;
  text-decoration: none;
  transition: all 0.3s;
  box-shadow: 0 2px 6px rgba(106, 86, 198, 0.2);
}

.section-header .more-link:hover {
  background-color: #7b68d7;
  box-shadow: 0 4px 10px rgba(106, 86, 198, 0.3);
  transform: translateY(-2px);
  text-decoration: none;
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
  height: 480px;
  display: flex;
  border: none !important;

  :deep(.el-card__body) {
    flex: 1;
    padding: 25px;
    display: flex;
    flex-direction: column;
  }

  .carousel-image {
    height: 200px;
    margin: -25px -25px 20px -25px;
    overflow: hidden;
    border-radius: 8px 8px 0 0;

    .news-image {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }
  }

  /* 更改标题颜色 */
  .carousel-title {
    color: #6a56c6;
    font-size: 1.6rem;
    line-height: 1.4;
    margin: 0 0 15px;
    font-weight: 600;
  }

  .carousel-content {
    flex: 1;
    color: var(--el-text-color-regular);
    line-height: 1.6;
    overflow: hidden;
    display: -webkit-box;
    margin-bottom: 15px;
  }

  .card-content {
    flex: 1;
    display: flex;
    flex-direction: column;
    padding-bottom: 60px;
  }

  /* 修改阅读全文按钮 */
  .read-more {
    position: absolute;
    left: 25px;
    bottom: 25px;
    margin-top: 0;
    background-color: #6a56c6;
    border-color: #6a56c6;
    color: white;
    font-weight: 500;
  }

  .read-more:hover {
    background-color: #7b68d7;
    border-color: #7b68d7;
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
  cursor: pointer;

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

  /* 修改新闻标题样式 */
  .news-title {
    font-size: 1.1rem;
    line-height: 1.4;
    margin: 0;
    color: var(--el-text-color-primary);
    display: -webkit-box;
    overflow: hidden;
    transition: color 0.2s;
  }

  &:hover {
    transform: translateY(-5px);
    box-shadow: 0 10px 20px rgba(106, 86, 198, 0.1) !important;
    border-color: #6a56c6;
  }
  
  &:hover .news-title {
    color: #6a56c6;
  }
}

.time {
  color: var(--el-text-color-secondary);
  font-size: 0.85rem;
  margin-top: 8px;
}

/* 自定义按钮样式 */
:deep(.el-button--primary) {
  background-color: #6a56c6;
  border-color: #6a56c6;
}

:deep(.el-button--primary:hover) {
  background-color: #7b68d7;
  border-color: #7b68d7;
}

@media (max-width: 768px) {
  .section-column {
    padding: 0;
    margin-bottom: 30px;

    &:last-child {
      margin-bottom: 0;
    }
  }

  /* 移动版增加更多按钮的明显度 */
  .section-header .more-link {
    padding: 6px 12px;
    font-size: 0.9rem;
  }

  .carousel-card {
    height: 360px;

    .carousel-title {
      font-size: 1.4rem;
    }
  }
}
</style>