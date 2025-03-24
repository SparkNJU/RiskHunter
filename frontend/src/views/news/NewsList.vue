<script setup lang="ts">
import { ref, onMounted, computed, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getNewsListByType } from '../../api/news'

interface NewsItem {
    id: number
    title: string
    time: string
    type: string
}

const route = useRoute()
const router = useRouter()
const newsType = computed(() => route.params.type as string)
const newsList = ref<NewsItem[]>([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(20)

const processNewsItem = (type: string, item: any): NewsItem => ({
    id: item.id,
    type,
    title: item.标题 || '无标题',
    time: item.发布时间 || '未知时间'
})

const loadData = async () => {
    try {
        loading.value = true
        const res = await getNewsListByType(newsType.value)
        newsList.value = res.map((item: any) => processNewsItem(newsType.value, item)).reverse()
    } finally {
        loading.value = false
    }
}

const paginatedData = computed(() => {
    const start = (currentPage.value - 1) * pageSize.value
    return newsList.value.slice(start, start + pageSize.value)
})

const rowClick = (row: NewsItem) => {
    router.push(`/news/${row.type}/${row.id}`)
}

onMounted(loadData)

// 新增路由参数监听
watch(newsType, () => {
    currentPage.value = 1
    loadData()
})

</script>

<template>
    <el-main class="list-container">
        <el-card v-loading="loading">
            <div class="button-group">
                <el-button @click="router.push('/news')" type="primary" plain>
                    返回
                </el-button>
                <el-button v-if="newsType === 'government'" @click="router.push('/news/market')" type="success" plain>
                    市场动态
                </el-button>
                <el-button v-else @click="router.push('/news/government')" type="success" plain>
                    政事要闻
                </el-button>
            </div>
            
            <div class="table-header">
                <h2>{{ newsType === 'government' ? '政事要闻' : '市场动态' }}</h2>
                <span class="total-count">共 {{ newsList.length }} 条记录</span>
            </div>

            <el-table :data="paginatedData" @row-click="rowClick" class="news-table" style="width: 100%">
                <el-table-column prop="title" label="新闻标题" min-width="300">
                    <template #default="{ row }">
                        <span class="title-text">{{ row.title }}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="time" label="发布时间" width="180" />
                <el-table-column label="操作" width="120">
                    <template #default="{ row }">
                        <el-button type="primary" size="small" @click.stop="rowClick(row)">
                            详情
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>

            <el-pagination v-model:current-page="currentPage" v-model:page-size="pageSize" :page-sizes="[20, 50, 100]"
                layout="total, sizes, prev, pager, next" :total="newsList.length" class="pagination" />
        </el-card>
    </el-main>
</template>

<style scoped>
.button-group {
    margin-bottom: 15px;
    width: 100%;
    display: flex;
    justify-content: space-between;
}

/* 修改按钮样式 */
:deep(.el-button--primary) {
    background-color: #6a56c6;
    border-color: #6a56c6;
    color: white;
    font-weight: 500;
}

:deep(.el-button--primary:hover) {
    background-color: #7b68d7;
    border-color: #7b68d7;
}

:deep(.el-button--primary.is-plain) {
    color: #6a56c6;
    background: rgba(106, 86, 198, 0.1);
    border-color: #6a56c6;
}

:deep(.el-button--primary.is-plain:hover) {
    background-color: #6a56c6;
    color: white;
}

:deep(.el-button--success.is-plain) {
    color: #6a56c6;
    background: rgba(106, 86, 198, 0.1);
    border-color: #6a56c6;
}

:deep(.el-button--success.is-plain:hover) {
    background-color: #6a56c6;
    color: white;
    border-color: #6a56c6;
}

:deep(.el-card) {
    width: 80% !important;
    max-width: 100%;
}

.table-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 24px;
    padding: 0 16px;
}

/* 修改标题颜色 */
.table-header h2 {
    color: #6a56c6;
    font-weight: 600;
}

.total-count {
    color: var(--el-text-color-secondary);
    font-size: 0.9rem;
}

.news-table {
    margin: 20px 0;

    :deep(.el-table__row) {
        cursor: pointer;
        transition: background-color 0.2s;

        &:hover {
            background-color: rgba(106, 86, 198, 0.05); /* 更改悬停背景色 */
        }
    }
    
    /* 修改表头颜色 */
    :deep(.el-table__header-wrapper th) {
        background-color: rgba(106, 86, 198, 0.1);
    }
}

.title-text {
    overflow: hidden;
    color: var(--el-text-color-primary);
    transition: color 0.2s;
}

.news-table :deep(.el-table__row:hover) .title-text {
    color: #6a56c6; /* 悬停时标题变紫色 */
}

.pagination {
    margin-top: 24px;
    justify-content: flex-end;
}

/* 移动端样式保持不变 */
@media (max-width: 768px) {
    /* 保留原有响应式样式 */
}
</style>