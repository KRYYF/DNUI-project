<template>
  <div class="page">
    <div class="page-header"><h3>AQI指数分布统计</h3></div>

    <el-card v-loading="loading" shadow="never" class="chart-card">
      <div ref="chartRef" class="chart"></div>
    </el-card>

    <el-card shadow="never" class="table-card">
      <el-table :data="tableData" stripe border style="width: 100%">
        <el-table-column prop="level" label="AQI等级" width="120" />
        <el-table-column prop="grade" label="等级说明" width="150" />
        <el-table-column prop="count" label="检测数量" sortable />
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import { getAqiDistribution } from '../../api/statistics'

const loading = ref(false)
const tableData = ref([])
const chartRef = ref(null)
let chartInstance = null

const gradeColors = {
  '优': '#66BB6A', '良': '#A5D6A7', '轻度污染': '#FFCC02',
  '中度污染': '#FF9800', '重度污染': '#EF5350', '严重污染': '#B71C1C',
}

onMounted(async () => {
  loading.value = true
  try {
    const res = await getAqiDistribution()
    tableData.value = res.data
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
    tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
    legend: { orient: 'vertical', right: '5%', top: 'center' },
    series: [{
      type: 'pie',
      radius: ['40%', '70%'],
      center: ['40%', '50%'],
      avoidLabelOverlap: false,
      itemStyle: {
        borderRadius: 6,
        borderColor: '#fff',
        borderWidth: 2,
      },
      label: { show: true, formatter: '{b}\n{d}%' },
      data: data.map((d) => ({
        name: d.grade,
        value: d.count,
        itemStyle: { color: gradeColors[d.grade] || '#909399' },
      })),
    }],
  }
  chartInstance.setOption(option)
}
</script>

<style scoped>
.page { max-width: 1000px; }
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
.chart-card {
  margin-bottom: 16px;
  border-radius: 14px;
  border: 1px solid #eef3ee;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.04);
}
.table-card {
  border-radius: 14px;
  border: 1px solid #eef3ee;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.04);
}
.chart { width: 100%; height: 400px; }
</style>
