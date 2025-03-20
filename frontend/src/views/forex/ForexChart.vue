<script setup lang="ts">
import { ref, onMounted, watch, onBeforeUnmount } from 'vue'
import * as echarts from 'echarts'

const chartRef = ref<HTMLElement>()
let chartInstance: echarts.ECharts | null = null

interface Props {
  chartData: {
    xData: string[]
    yData: number[]
    meta: {
      name: string
      unit: string
      source: string
      indicatorId: string
    }
  }
}

const props = defineProps<Props>()

const initChart = () => {
  if (!chartRef.value) return

  if (chartInstance) {
    chartInstance.dispose()
    chartInstance = null
  }

  chartInstance = echarts.init(chartRef.value)
  const option = getChartOption()
  chartInstance.setOption(option)
}

const getChartOption = (): echarts.EChartsOption => {
  const splitDate = new Date('2025-01-01')
  const actualData: [string, number][] = []
  const predictData: [string, number][] = []

  props.chartData.xData.forEach((dateStr, index) => {
    const currentDate = new Date(dateStr.length == 7 ? dateStr + '-01' : dateStr)
    const value = props.chartData.yData[index]
    if (currentDate < splitDate) {
      actualData.push([dateStr, value])
    } else {
      predictData.push([dateStr, value])
    }
  })

  return {
    title: {
      text: `${props.chartData.meta.name} `,
      subtext: [
        props.chartData.meta.unit && `单位: ${props.chartData.meta.unit}`,
        `数据来源：${props.chartData.meta.source}`,
        `指标ID：${props.chartData.meta.indicatorId}`
      ].filter(Boolean).join(', '),
      left: 'center'
    },
    tooltip: {
      trigger: 'axis',
      formatter: (params: any) => {
        const data = params[0]
        const seriesType = data.seriesName === '预测数据' ? '（预测）' : ''
        return `
          ${data.axisValue}<br/>
          ${props.chartData.meta.name}${seriesType}：${data.value[1]} ${props.chartData.meta.unit}
        `
      }
    },
    xAxis: {
      type: 'category',
      data: props.chartData.xData,
      axisLabel: {
        formatter: (value: string) => value.replace('-', '年') + '月'
      }
    },
    yAxis: {
      type: 'value',
      name: props.chartData.meta.unit || ''
    },
    series: [
      {
        name: '实际数据',
        type: 'line',
        data: actualData,
        smooth: true,
        itemStyle: {
          color: '#66b1ff' // 浅蓝色
        },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(102, 177, 255, 0.6)' },
            { offset: 1, color: 'rgba(102, 177, 255, 0.01)' }
          ])
        }
      },
      {
        name: '预测数据',
        type: 'line',
        data: predictData,
        smooth: true,
        itemStyle: {
          color: '#ffa940' // 浅橙色
        },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(255, 169, 64, 0.6)' },
            { offset: 1, color: 'rgba(255, 169, 64, 0.01)' }
          ])
        }
      }
    ]
  }
}

// 响应式调整
const handleResize = () => chartInstance?.resize()
onMounted(initChart)
onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize)
  chartInstance?.dispose()
})

watch(() => props.chartData, () => {
  initChart()
  window.addEventListener('resize', handleResize)
}, { deep: true, flush: 'post' })
</script>

<template>
  <div ref="chartRef" class="chart-container"></div>
</template>

<style scoped>
.chart-container {
  width: 100%;
  height: 700px;
}
</style>
