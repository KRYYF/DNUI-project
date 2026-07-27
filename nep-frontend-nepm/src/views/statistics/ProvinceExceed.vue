<template>
  <div class="page">
    <div class="page-header"><h3>省分组检查统计</h3></div>

    <el-card v-loading="loading" shadow="never" class="chart-card">
      <div ref="chartRef" class="chart"></div>
    </el-card>

    <el-card shadow="never" class="table-card">
      <el-table :data="tableData" stripe border style="width: 100%">
        <el-table-column prop="provinceName" label="省份" width="120" />
        <el-table-column prop="so2Exceed" label="SO₂超标数量" width="140" sortable />
        <el-table-column prop="coExceed" label="CO超标数量" width="140" sortable />
        <el-table-column prop="pm25Exceed" label="PM2.5超标数量" width="150" sortable />
        <el-table-column prop="aqiExceed" label="AQI超标数量" width="140" sortable />
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import { getProvinceExceed } from '../../api/statistics'

const loading = ref(false)
const tableData = ref([])
const chartRef = ref(null)
let chartInstance = null

onMounted(async () => {
  loading.value = true
  try {
    const res = await getProvinceExceed()
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

  const provinces = data.map((d) => d.provinceName)
  const option = {
    tooltip: { trigger: 'axis' },
    legend: { data: ['SO₂超标', 'CO超标', 'PM2.5超标', 'AQI超标'] },
    grid: { left: '3%', right: '4%', bottom: '10%', containLabel: true },
    xAxis: {
      type: 'category',
      data: provinces,
      axisLabel: { rotate: 45 },
    },
    yAxis: { type: 'value', name: '超标累计数量' },
    series: [
      {
        name: 'SO₂超标', type: 'bar', data: data.map((d) => d.so2Exceed),
        itemStyle: { color: '#66BB6A' },
        emphasis: { itemStyle: { color: '#43A047' } },
      },
      {
        name: 'CO超标', type: 'bar', data: data.map((d) => d.coExceed),
        itemStyle: { color: '#81C784' },
        emphasis: { itemStyle: { color: '#4CAF50' } },
      },
      {
        name: 'PM2.5超标', type: 'bar', data: data.map((d) => d.pm25Exceed),
        itemStyle: { color: '#A5D6A7' },
        emphasis: { itemStyle: { color: '#66BB6A' } },
      },
      {
        name: 'AQI超标', type: 'bar', data: data.map((d) => d.aqiExceed),
        itemStyle: { color: '#EF5350' },
        emphasis: { itemStyle: { color: '#D32F2F' } },
      },
    ],
  }
  chartInstance.setOption(option)
}
</script>

<style scoped>
.page { max-width: 1300px; }
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
