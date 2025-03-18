<script setup lang="ts">
import { inject, ref, watch } from 'vue';
import { ElMessage } from 'element-plus';
import { Search, Refresh } from '@element-plus/icons-vue'
import ForexChart from './ForexChart.vue'
import { getAllForexTypes, getAllForexList, getForexByName } from '../../api/forex';

// 窗口监听
const viewport = inject('viewport', {
  isMobile: ref(false),
  viewportWidth: ref(0),
  breakpoints: { md: 768 }
})

const loading = ref(false)

// 数据名称选项, 只能在前端写死
const typeOptions = getAllForexTypes().map(t => ({ value: t.type, label: t.name }))
const dataOptions = getAllForexList()

const queryForm = ref({
  dataType: 'CHN',
  dataName: '人民币：实际有效汇率指数',
  startTime: '',
  endTime: '',
})

interface ChartData {
  xData: string[]
  yData: number[]
  meta: {
    name: string
    unit: string
    source: string
    indicatorId: string
  }
}
const chartData = ref<ChartData | null>(null)

// 重置表单
const handleReset = () => {
  queryForm.value = {
    dataType: 'CHN',
    dataName: dataOptions['CHN'][0],
    startTime: '',
    endTime: '',
  }
  handleSearch()
}

// 搜索
const handleSearch = async () => {
  try {
    loading.value = true
    const data = await getForexByName(
      queryForm.value.dataType,
      queryForm.value.dataName
    )

    // 解析数据
    const meta = data.元数据[0]
    const indicatorKey = Object.keys(data.指标[0]).find(k => k !== '日期')!

    const parseDate = (dateStr: string) => {
      const parts = dateStr.split('-')
      return parts.length === 2
        ? new Date(`${dateStr}-01`) // 补充日期为当月第一天
        : new Date(dateStr)
    }

    // 过滤数据
    const filteredData = data.指标.filter((d: any) => {
      const currentDate = parseDate(d.日期)
      const startTime = queryForm.value.startTime ? new Date(queryForm.value.startTime) : null
      const endTime = queryForm.value.endTime ? new Date(queryForm.value.endTime) : null

      return (!startTime || currentDate >= startTime) &&
        (!endTime || currentDate <= endTime)
    })

    chartData.value = {
      xData: filteredData.map((d: any) => d['日期']),
      yData: filteredData.map((d: any) => d[indicatorKey]),
      meta: {
        name: meta.指标名称,
        unit: meta.单位,
        source: meta.来源,
        indicatorId: meta.指标ID
      }
    }
  } catch (error) {
    ElMessage.error('数据加载失败')
    console.error(error)
  } finally {
    loading.value = false
  }
}

handleSearch()

watch(() => queryForm.value.dataType, (newType) => {
  // 获取当前类型下的第一个数据名称作为默认值
  const firstOption = dataOptions[newType][0]
  queryForm.value.dataName = firstOption
})
</script>

<template>
  <el-main>
    <!-- 筛选栏 -->
    <el-card class="forex-card" :body-style="{ padding: '20px' }">
      <template #header>
        <div class="forex-header">
          <div class="forex-header-left">
            <el-icon class="forex-header-icon">
              <Search />
            </el-icon>
            <span class="forex-title">筛选条件</span>
          </div>
          <div class="forex-button-group">
            <el-button type="primary" @click="handleSearch" :disabled="queryForm.dataName.trim() === ''">
              <el-icon>
                <Search />
              </el-icon>查询
            </el-button>
            <el-button @click="handleReset">
              <el-icon>
                <Refresh />
              </el-icon>重置
            </el-button>
          </div>
        </div>
      </template>

      <el-form :model="queryForm" label-position="top">
        <el-row :gutter="20" :class="{ 'mobile-row': viewport.isMobile.value }">
          <el-col :xs="24" :sm="24" :md="12">
            <el-form-item label="数据类型">
              <el-select v-model="queryForm.dataType" placeholder="请选择数据类型" filterable>
                <el-option v-for="item in typeOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="12">
            <el-form-item label="数据名称">
              <el-select v-model="queryForm.dataName" placeholder="请选择数据名称" filterable>
                <el-option v-for="item in dataOptions[queryForm.dataType]" :key="item" :label="item" :value="item" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 高级选项 -->
        <el-collapse class="advanced-options">
          <el-collapse-item name="advanced" title="高级选项">
            <el-row :gutter="20" :class="{ 'mobile-row': viewport.isMobile.value }">
              <el-col :xs="24" :sm="24" :md="12">
                <el-form-item label="起始时间">
                  <el-date-picker v-model="queryForm.startTime" type="datetime" placeholder="选择起始时间" />
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="24" :md="12">
                <el-form-item label="结束时间">
                  <el-date-picker v-model="queryForm.endTime" type="datetime" placeholder="选择结束时间" />
                </el-form-item>
              </el-col>
            </el-row>
          </el-collapse-item>
        </el-collapse>
      </el-form>
    </el-card>

    <!-- 图表 -->
    <el-card v-if="chartData" class="forex-card">
      <ForexChart :chart-data="chartData" />
    </el-card>
  </el-main>
</template>

<style scoped>
.forex-card {
  width: 75vw;
  margin-bottom: 1rem;
}

@media screen and (max-width: 768px) {
  .forex-card {
    width: 100vw;
  }

  .mobile-row {
    margin-left: 0 !important;
    margin-right: 0 !important;
  }
}

/* 卡片顶栏 */
.forex-header {
  height: 2rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.forex-header-left {
  display: flex;
  align-items: center;
  gap: 8px;
}

.forex-title {
  font-size: 1.1em;
  font-weight: 600;
  color: var(--el-text-color-primary);
}

.forex-header-icon {
  font-size: 1.1em;
  color: var(--el-color-primary);
}

.forex-button-group {
  display: flex;
  gap: 12px;
  margin-top: 1.5rem;
}

.advanced-options {
  :deep(.el-collapse-item__header) {
    font-size: var(--el-form-label-font-size);
    font-weight: 500;
  }
}

:deep(.el-input__wrapper),
:deep(.el-select__wrapper),
:deep(.el-select .el-input__wrapper),
:deep(.el-date-editor.el-input__wrapper),
:deep(.el-date-editor--datetime),
:deep(.el-date-editor--date) {
  height: 2rem;
  width: 100%;
}
</style>
