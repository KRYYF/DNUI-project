<template>
  <div class="page">
    <div class="page-header"><h3>其它数据统计</h3></div>

    <el-row :gutter="20">
      <!-- 空气质量检测数量实时统计 -->
      <el-col :span="8">
        <el-card shadow="never" class="count-card">
          <template #header>
            <span>空气质量检测数量实时统计</span>
          </template>
          <div class="count-body">
            <div class="count-item">
              <div class="count-num total">{{ realTimeData.totalCount }}</div>
              <div class="count-label">检测总数</div>
            </div>
            <div class="count-item">
              <div class="count-num assigned">{{ realTimeData.goodCount }}</div>
              <div class="count-label">良好数量</div>
            </div>
            <div class="count-item">
              <div class="count-num confirmed">{{ realTimeData.pollutionCount }}</div>
              <div class="count-label">超标数量</div>
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- 省覆盖率仪表盘 -->
      <el-col :span="8">
        <el-card v-loading="loading" shadow="never">
          <template #header>
            <span>全国省份覆盖率</span>
          </template>
          <div ref="provinGaugeRef" class="gauge-chart"></div>
          <div v-if="coverageData" class="coverage-text">
            {{ coverageData.provinceCovered }} / {{ coverageData.provinceTotal }} 省
          </div>
        </el-card>
      </el-col>

      <!-- 大城市覆盖率仪表盘 -->
      <el-col :span="8">
        <el-card v-loading="loading" shadow="never">
          <template #header>
            <span>全国大城市覆盖率</span>
          </template>
          <div ref="cityGaugeRef" class="gauge-chart"></div>
          <div v-if="coverageData" class="coverage-text">
            {{ coverageData.cityCovered }} / {{ coverageData.cityTotal }} 城
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import { getRealTimeCount, getGridCoverage } from '../../api/statistics'

const loading = ref(false)
const realTimeData = reactive({ totalCount: 0, goodCount: 0, pollutionCount: 0 })
const coverageData = ref(null)
const provinGaugeRef = ref(null)
const cityGaugeRef = ref(null)
let provinGauge = null
let cityGauge = null

onMounted(async () => {
  loading.value = true
  try {
    const [rtRes, gcRes] = await Promise.all([getRealTimeCount(), getGridCoverage()])
    Object.assign(realTimeData, rtRes.data)
    coverageData.value = gcRes.data
    await nextTick()
    renderProvinGauge(gcRes.data)
    renderCityGauge(gcRes.data)
  } finally {
    loading.value = false
  }
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  if (provinGauge) provinGauge.dispose()
  if (cityGauge) cityGauge.dispose()
})

function handleResize() {
  if (provinGauge) provinGauge.resize()
  if (cityGauge) cityGauge.resize()
}

function makeGaugeOption(value) {
  return {
    series: [{
      type: 'gauge',
      startAngle: 200,
      endAngle: -20,
      center: ['50%', '60%'],
      radius: '85%',
      min: 0,
      max: 100,
      axisLine: {
        lineStyle: {
          width: 20,
          color: [[0.3, '#EF5350'], [0.7, '#FFB74D'], [1, '#66BB6A']],
        },
      },
      pointer: { length: '60%', width: 8, itemStyle: { color: '#43A047' } },
      axisTick: { distance: -20, length: 6 },
      splitLine: { distance: -25, length: 16 },
      axisLabel: { distance: 30, fontSize: 12 },
      detail: {
        valueAnimation: true,
        formatter: '{value}%',
        fontSize: 24,
        offsetCenter: [0, '75%'],
        color: '#2E7D32',
      },
      data: [{ value: value || 0 }],
    }],
  }
}

function renderProvinGauge(data) {
  if (!provinGaugeRef.value) return
  provinGauge = echarts.init(provinGaugeRef.value)
  provinGauge.setOption(makeGaugeOption(data.provinceCoverage))
}

function renderCityGauge(data) {
  if (!cityGaugeRef.value) return
  cityGauge = echarts.init(cityGaugeRef.value)
  cityGauge.setOption(makeGaugeOption(data.cityCoverage))
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
.page :deep(.el-card) {
  border-radius: 14px;
  border: 1px solid #eef3ee;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.04);
}
.page :deep(.el-card__header) {
  font-weight: 600;
  color: #1a1a2e;
  border-bottom: 1px solid #eef3ee;
}
.count-body { display: flex; justify-content: space-around; padding: 20px 0; }
.count-item { text-align: center; }
.count-num { font-size: 40px; font-weight: 800; line-height: 1.2; }
.count-num.total { color: #43A047; }
.count-num.assigned { color: #FF9800; }
.count-num.confirmed { color: #F44336; }
.count-label { font-size: 14px; color: #909399; margin-top: 8px; }
.gauge-chart { width: 100%; height: 250px; }
.coverage-text { text-align: center; margin-top: 4px; color: #606266; font-size: 14px; font-weight: 500; }
</style>
