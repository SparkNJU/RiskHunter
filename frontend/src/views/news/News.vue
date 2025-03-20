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
    content.value = res.content
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
  }

  h1 {
    font-size: 24px;
    margin-bottom: 15px;
    color: #333;
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
      color: #409eff;
      text-decoration: none;

      &:hover {
        text-decoration: underline;
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
  }
}
</style>
