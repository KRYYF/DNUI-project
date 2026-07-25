<template>
  <div class="page">
    <div class="page-header"><h3>AQI指数趋势统计</h3></div>

    <el-card v-loading="loading" shadow="never">
      <div ref="chartRef" class="chart"></div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import { getAqiTrend } from '../../api/statistics'

const loading = ref(false)
const chartRef = ref(null)
let chartInstance = null

onMounted(async () => {
  loading.value = true
  try {
    const res = await getAqiTrend()
    await nextTick()
    renderChart(res.data)
  } finally {
    loading.value = false
  }
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  if (chartInstance) chartInstance.dispose()
})

function handleResize() {
  if (chartInstance) chartInstance.resize()
}

function renderChart(data) {
  if (!chartRef.value) return
  chartInstance = echarts.init(chartRef.value)

  const option = {
    tooltip: { trigger: 'axis' },
    grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
    xAxis: {
      type: 'category',
      data: data.months || data.labels || [],
      axisLabel: { rotate: 45 },
      name: '月份',
    },
    yAxis: {
      type: 'value',
      name: '超标记录数',
      minInterval: 1,
    },
    series: [{
      name: '超标数量',
      type: 'line',
      data: data.exceedCounts || data.values || [],
      smooth: true,
      lineStyle: { color: '#F56C6C', width: 3 },
      itemStyle: { color: '#F56C6C' },
      areaStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: 'rgba(245, 108, 108, 0.3)' },
          { offset: 1, color: 'rgba(245, 108, 108, 0.05)' },
        ]),
      },
      markLine: {
        silent: true,
        data: [{ type: 'average', name: '平均值' }],
        lineStyle: { color: '#409EFF', type: 'dashed' },
      },
    }],
  }
  chartInstance.setOption(option)
}
</script>

<style scoped>
.page { max-width: 1100px; }
.page-header h3 { margin: 0 0 16px; }
.chart { width: 100%; height: 400px; }
</style>
