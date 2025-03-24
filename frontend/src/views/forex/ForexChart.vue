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
          legend: {
      data: ['实际数据', '预测数据'],
      textStyle: {
        color: '#6a56c6'
      },
      itemStyle: {
        borderColor: '#6a56c6'
      },
      // 将图例放置在右上角
      right: '5%',
      top: '5%',
      orient: 'horizontal',
      padding: [5, 10]
    },
    title: {
      text: `${props.chartData.meta.name} `,
      subtext: [
        props.chartData.meta.unit && `单位: ${props.chartData.meta.unit}`,
        `数据来源：${props.chartData.meta.source}`,
        `指标ID：${props.chartData.meta.indicatorId}`
      ].filter(Boolean).join(', '),

      left: 'center',
      textStyle: {
        color: '#6a56c6'
      },
      subtextStyle: {
        color: '#8a7bd1'
      }
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
      },
      backgroundColor: 'rgba(106, 86, 198, 0.8)',
      borderColor: '#6a56c6',
      textStyle: {
        color: '#ffffff'
      }
    },
    xAxis: {
      type: 'category',
      data: props.chartData.xData,
      axisLabel: {
        formatter: (value: string) => value.replace('-', '年') + '月',
        color: '#6a56c6'
      },
      axisLine: {
        lineStyle: {
          color: '#d4cdf3'
        }
      }
    },
    yAxis: {
      type: 'value',
      name: props.chartData.meta.unit || '',
      nameTextStyle: {
        color: '#6a56c6'
      },
      axisLabel: {
        color: '#6a56c6'
      },
      splitLine: {
        lineStyle: {
          color: '#eae7f8'
        }
      }
    },
    series: [
      {
        name: '实际数据',
        type: 'line',
        data: actualData,
        smooth: true,
        itemStyle: {
          color: '#6a56c6' // 主紫色
        },
        lineStyle: {
          width: 3,
          color: '#6a56c6'
        },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(106, 86, 198, 0.5)' },
            { offset: 1, color: 'rgba(106, 86, 198, 0.05)' }
          ])
        }
      },
      {
        name: '预测数据',
        type: 'line',
        data: predictData,
        smooth: true,
        itemStyle: {
          color: '#9e8be0' // 浅紫色
        },
        lineStyle: {
          width: 3,
          color: '#9e8be0',
          type: 'dashed'
        },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(158, 139, 224, 0.5)' },
            { offset: 1, color: 'rgba(158, 139, 224, 0.05)' }
          ])
        }
      }
    ],
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      top: '15%',
      containLabel: true
    },
    color: ['#6a56c6', '#9e8be0'],
    backgroundColor: 'rgba(234, 231, 248, 0.1)'
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
  background-color: rgba(234, 231, 248, 0.1);
  border-radius: 8px;
  padding: 16px;
}
</style>