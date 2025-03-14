<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getNewsById } from '../../api/news'

interface NewsContent {
  id: number
  content: string
  source_file: string
}

const route = useRoute()
const content = ref('')
const loading = ref(false)

const loadData = async () => {
  try {
    loading.value = true
    const id = Number(route.params.newsId)
    getNewsById(id).then(res => {
      content.value = res.content
    })
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
  <div class="news-detail">
    <el-card v-loading="loading">
      <div class="content" v-html="content"></div>
    </el-card>
  </div>
</template>

<style scoped lang="scss">
.news-detail {
  padding: 20px;

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
