<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getNewsById } from '../../api/news'


const route = useRoute()
const router = useRouter()
const title = ref('')
const date = ref('')
const content = ref('')
const url = ref('')
const loading = ref(false)

const newsType = computed(() => route.params.type as string)

const loadData = async () => {
  try {
    const id = Number(route.params.newsId)
    const res = await getNewsById(newsType.value, id)
    title.value = res.title
    date.value = res.date
    content.value = res.content.replace(/<h1>.*?<\/h1>/gi, '').replace(/<p>\d{4}-\d{2}-\d{2}<\/p>/gi, '').trim()
    url.value = res.url
  } catch (error) {
    ElMessage.error('新闻加载失败')
  }
}

loadData()
</script>

<template>
  <div class="news-detail">
    <div class="back-button">
      <el-button @click="router.back()" type="primary" plain>
        返回
      </el-button>
    </div>
    <el-card v-loading="loading">
      <h1>{{ title }}</h1>
      <div class="meta">
        <span class="date">{{ date }}</span>
        <a :href="url" target="_blank" v-if="url" class="original-link">
          原文链接
        </a>
      </div>
      <div class="content" v-html="content"></div>
    </el-card>
  </div>
</template>

<style scoped lang="scss">
.news-detail {
  padding: 20px;

  .back-button {
    margin-bottom: 15px;
    
    /* 更明显的按钮样式 */
    :deep(.el-button) {
      background-color: #6a56c6; /* 紫色背景 */
      color: white; /* 白色文字 */
      border-color: #6a56c6; /* 紫色边框 */
      font-weight: 500; /* 更粗的字体 */
      
      &:hover {
        background-color: #7b68d7; /* 悬停时稍微亮一点 */
        border-color: #7b68d7;
      }
      
      .el-icon {
        color: white; /* 确保图标也是白色 */
      }
    }
  }

  h1 {
    font-size: 24px;
    margin-bottom: 15px;
    color: #6a56c6; /* 修改标题为紫色 */
    font-weight: 600; /* 稍微加粗 */
  }

  .meta {
    display: flex;
    justify-content: space-between;
    margin-bottom: 20px;
    color: #666;

    .date {
      font-size: 14px;
    }

    .original-link {
      color: #6a56c6; /* 修改链接为紫色 */
      text-decoration: none;
      font-weight: 500; /* 加粗 */
      padding: 5px 10px; /* 添加内边距 */
      border: 1px solid #6a56c6; /* 添加边框 */
      border-radius: 4px; /* 圆角边框 */
      transition: all 0.3s; /* 平滑过渡效果 */

      &:hover {
        background-color: #6a56c6; /* 悬停时背景变紫 */
        color: white; /* 悬停时文字变白 */
        text-decoration: none; /* 不需要下划线 */
      }
    }
  }

  .content {
    line-height: 1.6;
    font-size: 16px;

    :deep(img) {
      max-width: 100%;
      height: auto;
      margin: 15px 0;
    }
    
    /* 内容中的标题也使用紫色 */
    :deep(h2), :deep(h3), :deep(h4) {
      color: #6a56c6;
    }
    
    /* 内容中的链接使用紫色 */
    :deep(a) {
      color: #6a56c6;
      
      &:hover {
        text-decoration: underline;
      }
    }
  }
}
</style>