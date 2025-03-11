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
        return `
          ${data.axisValue}<br/>
          ${props.chartData.meta.name}：${data.value} ${props.chartData.meta.unit}
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
    series: [{
      data: props.chartData.yData,
      type: 'line',
      smooth: true,
      areaStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: 'rgba(64, 158, 255, 0.6)' },
          { offset: 1, color: 'rgba(64, 158, 255, 0.01)' }
        ])
      }
    }]
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
