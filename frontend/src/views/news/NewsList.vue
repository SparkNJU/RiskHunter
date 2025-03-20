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
            background-color: var(--el-fill-color-light);
        }
    }
}

.title-text {
    overflow: hidden;
}

.pagination {
    margin-top: 24px;
    justify-content: flex-end;
}

@media (max-width: 768px) {
    .list-container {
        width: 100%;
        padding: 10px;
    }

    :deep(.el-card) {
        width: 100% !important;
        box-shadow: none;
    }

    .table-header {
        flex-direction: column;
        align-items: flex-start;
        margin-bottom: 16px;

        h2 {
            font-size: 1.2rem;
            margin-bottom: 8px;
        }
    }

    .news-table {
        margin: 10px 0;

        :deep(.el-table__row) td {
            padding: 8px 0;
        }

        :deep(.el-table-column--selection .cell) {
            padding-left: 10px;
        }
    }

    .el-table-column:last-child {
        display: none;
    }

    .el-table-column[prop="title"] {
        width: 280px;
    }

    .el-table-column[prop="time"] {
        width: 120px;
    }

    .pagination {
        padding: 0 10px;
        margin-top: 16px;
    }
}
</style>
