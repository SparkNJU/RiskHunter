<template>
    <div ref="chartRef" style="width: 100%; height: 300px;"></div>
  </template>
  
  <script setup lang="ts">
  import { ref, onMounted, watch } from 'vue';
  import * as echarts from 'echarts';
  
  const chartRef = ref(null);
  let chartInstance: echarts.ECharts | null = null;
  
  // 定义 props
  interface Props {
    riskIndex: number;
  }
  const props = defineProps<Props>();
  
  // 监听 riskIndex 的变化
  watch(
    () => props.riskIndex,
    (newRiskIndex) => {
      if (chartInstance) {
        updateChart(newRiskIndex);
      }
    },
    { immediate: true } // 立即执行一次，确保初始渲染
  );
  
  onMounted(() => {
    // 初始化 ECharts 实例
    chartInstance = echarts.init(chartRef.value);
    updateChart(props.riskIndex);
  });
  
  const updateChart = (riskIndex: number) => {
    const option: echarts.EChartsOption = {
      series: [
        {
          type: 'gauge',
          min: 0,
          max: 100,
          splitNumber: 10,
          radius: '90%',
          axisLine: {
            lineStyle: {
              width: 10,
              color: [
                [0.2, '#409EFF'], // 浅蓝色
                [0.8, '#67C23A'], // 绿色
                [1, '#F56C6C']  // 深红色
              ]
            }
          },
          pointer: {
            itemStyle: {
              color: 'inherit'
            }
          },
          axisTick: {
            distance: -30,
            length: 8,
            lineStyle: {
              color: '#fff',
              width: 2
            }
          },
          splitLine: {
            distance: -30,
            length: 15,
            lineStyle: {
              color: '#fff',
              width: 3
            }
          },
          axisLabel: {
            color: 'rgba(255, 255, 255, 0.8)',
            distance: -50,
            fontSize: 12
          },
          title: {
            offsetCenter: [0, '-10%'],
            fontSize: 16
          },
          detail: {
            fontSize: 20,
            offsetCenter: [0, '30%'],
            valueAnimation: true,
            formatter: '{value}%',
            color: 'inherit'
          },
          data: [
            {
              value: riskIndex,
              name: '风险指数'
            }
          ]
        }
      ]
    };
  
    chartInstance?.setOption(option);
  };
  </script>
  
  <style scoped>
  /* 可以添加一些样式来美化仪表盘 */
  </style>