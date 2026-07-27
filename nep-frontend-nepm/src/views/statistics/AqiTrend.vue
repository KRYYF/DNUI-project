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
      lineStyle: { color: '#43A047', width: 3 },
      itemStyle: { color: '#43A047' },
      areaStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: 'rgba(67, 160, 71, 0.3)' },
          { offset: 1, color: 'rgba(67, 160, 71, 0.03)' },
        ]),
      },
      markLine: {
        silent: true,
        data: [{ type: 'average', name: '平均值' }],
        lineStyle: { color: '#FF9800', type: 'dashed' },
      },
    }],
  }
  chartInstance.setOption(option)
}
</script>

<style scoped>
.page { max-width: 1100px; }
.page-header {
  margin-bottom: 20px;
}
.page-header h3 {
  margin: 0;
  font-size: 20px;
  font-weight: 700;
  color: #1a1a2e;
  letter-spacing: 1px;
}
.page :deep(.el-card) {
  border-radius: 14px;
  border: 1px solid #eef3ee;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.04);
}
.chart { width: 100%; height: 400px; }
</style>
