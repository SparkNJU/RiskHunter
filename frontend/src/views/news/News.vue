<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getNewsById } from '../../api/news'

const route = useRoute()
const content = ref('')
const loading = ref(false)

const newsType = computed(() => route.params.type as string)

const loadData = async () => {
  try {
    const id = Number(route.params.newsId)
    // 调用API时传入新闻类型
    const res = await getNewsById(newsType.value, id)
    content.value = res.content
  } catch (error) {
    ElMessage.error('新闻加载失败')
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
