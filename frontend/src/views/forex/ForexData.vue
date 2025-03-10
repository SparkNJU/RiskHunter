<script setup lang="ts">
import { inject, onMounted, ref } from 'vue';
import { CurrencyList } from '../../utils'
import { Search, Refresh } from '@element-plus/icons-vue'
import * as echarts from 'echarts'

// 窗口监听
const viewport = inject('viewport', {
  isMobile: ref(false),
  viewportWidth: ref(0),
  breakpoints: { md: 768 }
})

const loading = ref(false)

const queryForm = ref({
  dataName: '汇率',
  baseCurrency: '',
  targetCurrency: '',
  startTime: '',
  endTime: '',
})

const handleReset = () => {
  queryForm.value = {
    dataName: '汇率',
    baseCurrency: '',
    targetCurrency: '',
    startTime: '',
    endTime: '',
  }
}

const handleSearch = async () => {
  // TODO
  try {
    loading.value = true

    chartData.value = mockData.filter(item => {
      const itemDate = new Date(item.date)
      const start = queryForm.value.startTime ? new Date(queryForm.value.startTime) : null
      const end = queryForm.value.endTime ? new Date(queryForm.value.endTime) : null

      return (!start || itemDate >= start) && (!end || itemDate <= end)
    })

    renderChart()
  } finally {
    loading.value = false
  }
}

// 前端死数据
const mockData = [
  { date: '2024-10-01', value: 6.85 },
  { date: '2024-11-01', value: 6.87 },
  { date: '2024-12-01', value: 6.89 },
  { date: '2025-01-01', value: 6.91 },
  { date: '2025-02-01', value: 6.93 },
  { date: '2025-03-01', value: 6.95 }
]
// 图表
const chartRef = ref(null)
// 图表数据
const chartData = ref(mockData)
// 数据处理
const processData = (data: any[]) => {
  const splitDate = new Date('2025-01-01').getTime()
  return data.reduce((acc, cur) => {
    const date = new Date(cur.date).getTime()
    date >= splitDate ?
      acc.predict.push([cur.date, cur.value]) :
      acc.actual.push([cur.date, cur.value])
    return acc
  }, { actual: [], predict: [] })
}
let nowChar: echarts.ECharts | null = null
// 渲染图表
const renderChart = () => {
  if (nowChar === null) {
    nowChar = echarts.init(chartRef.value)
  }

  const { actual, predict } = processData(chartData.value)
  nowChar.setOption({
    // 用户悬停在图表数据点上时显示详细信息
    tooltip: {
      // 触发方式为坐标轴触发: 显示该横坐标下所有系列的数据
      trigger: 'axis',
      // 格式化信息
      formatter: (params: any) => {
        const date = params[0].axisValueLabel
        const value = params[0].data[1]
        return `${date}<br/>汇率: ${value}`
      }
    },
    // 横坐标
    xAxis: {
      type: 'time',
      splitLine: { show: false }
    },
    // 纵坐标
    yAxis: { type: 'value' },
    // 分组
    series: [
      {
        name: '实际汇率',
        type: 'line',
        data: actual,
        itemStyle: { color: '#67C23A' },
        lineStyle: { width: 2 },
        smooth: true
      },
      {
        name: '预测汇率',
        type: 'line',
        data: predict,
        itemStyle: { color: '#E6A23C' },
        lineStyle: {
          type: 'dashed',
          width: 2
        },
        symbol: 'triangle',
        symbolSize: 10
      }
    ]
  })
}

onMounted(() => {
  renderChart()
})
</script>

<template>
  <el-main>
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
            <el-form-item>
              <label class="forex-label" :class="{ 'error': queryForm.dataName.trim() === '' }"> {{
                queryForm.dataName.trim() === '' ? '数据名称不能为空' : '数据名称' }}
              </label>
              <el-input v-model="queryForm.dataName" :class="{ 'error-input': queryForm.dataName.trim() === '' }"
                placeholder="请输入数据名称" clearable />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20" :class="{ 'mobile-row': viewport.isMobile.value }">
          <el-col :xs="24" :sm="24" :md="12">
            <el-form-item label="基准货币">
              <el-select v-model="queryForm.baseCurrency" placeholder="请选择基准货币" clearable>
                <el-option v-for="currency in CurrencyList" :key="currency.code"
                  :label="`${currency.code} - ${currency.name}`" :value="currency.number" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="12">
            <el-form-item label="报价货币">
              <el-select v-model="queryForm.targetCurrency" placeholder="请选择报价货币" clearable>
                <el-option v-for="currency in CurrencyList" :key="currency.code"
                  :label="`${currency.code} - ${currency.name}`" :value="currency.number" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
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
      </el-form>
    </el-card>

    <div ref="chartRef" v-loading="loading" style="height:500px;width:100%" />
  </el-main>
</template>

<style scoped>
.forex-card {
  width: 75vw;
  margin-bottom: 1rem;
}

@media screen and (max-width: 768px) {
  .forex-card {
    width: 90vw;
  }

  .mobile-row {
    margin-left: 0 !important;
    margin-right: 0 !important;
  }

  :deep(.el-form-item__label) {
    font-size: 0.75rem;
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

.forex-label {
  margin-bottom: 0.5rem;
  color: var(--el-text-color-primary);
}

.forex-label.error {
  color: var(--el-color-danger);
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