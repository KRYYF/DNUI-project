<template>
  <div class="nepv-dashboard">
    <div class="dashboard-bg"></div>

    <header class="dashboard-header">
      <span class="datetime">{{ currentTime }}</span>
      <h1 class="header-title">东软空气质量环保公众监督平台</h1>
    </header>

    <!-- 断开连接警告条 -->
    <transition name="banner-fade">
      <div v-if="pausedByError" class="error-banner">
        <el-icon><WarningFilled /></el-icon>
        <span>后端连接断开，已暂停自动刷新</span>
        <el-button size="small" type="warning" @click="retryFetch" class="retry-btn">
          <el-icon><Refresh /></el-icon>重新连接
        </el-button>
      </div>
    </transition>

    <main class="dashboard-main">
      <section class="col col-left">
        <div class="gauge-row">
          <div class="card gauge-half">
            <div class="card-header">
              <span class="card-title">全国省网格覆盖率(%)</span>
            </div>
            <div class="card-body">
              <EChart :option="provinceCoverageOption" height="100%" />
            </div>
          </div>
          <div class="card gauge-half">
            <div class="card-header">
              <span class="card-title">全国大城市网格覆盖率(%)</span>
            </div>
            <div class="card-body">
              <EChart :option="cityCoverageOption" height="100%" />
            </div>
          </div>
        </div>
        <div class="card card-pie">
          <div class="card-header">
            <span class="card-title">空气质量指数级别分布</span>
          </div>
          <div class="card-body">
            <EChart :option="aqiPieOption" height="100%" />
          </div>
        </div>
        <div class="card card-line">
          <div class="card-header">
            <span class="card-title">12个月内空气质量超标趋势</span>
          </div>
          <div class="card-body">
            <EChart :option="trendOption" height="100%" />
          </div>
        </div>
      </section>

      <section class="col col-center">
        <div class="card map-card">
          <div class="card-header">
            <span class="card-title">全国空气质量超标分布地图</span>
            <span class="refresh-indicator" :class="connectionStatus">
              <span class="dot"></span>
              <template v-if="connectionStatus === 'disconnected'">连接断开</template>
              <template v-else-if="connectionStatus === 'polling'">实时监测中</template>
              <template v-else>空闲</template>
            </span>
          </div>
          <div class="card-body map-body">
            <div v-if="!mapReady" class="map-loading">
              <el-icon class="is-loading" :size="32"><Loading /></el-icon>
              <span>地图数据加载中…</span>
            </div>
            <EChart v-else :option="mapOption" height="100%" />
          </div>
        </div>
        <div class="kpi-bar">
          <div class="kpi-item kpi-total">
            <KpiCounter title="空气质量检测总数量" :value="kpi.totalCount" icon="DataAnalysis" />
          </div>
          <div class="kpi-item kpi-good">
            <KpiCounter title="空气质量良好数量" :value="kpi.goodCount" icon="CircleCheckFilled" />
          </div>
          <div class="kpi-item kpi-pollution">
            <KpiCounter title="空气质量污染数量" :value="kpi.pollutionCount" icon="WarningFilled" />
          </div>
        </div>
      </section>

      <section class="col col-right">
        <div class="card">
          <div class="card-header">
            <span class="card-title">二氧化硫(SO₂)浓度超标累计</span>
          </div>
          <div class="card-body">
            <EChart :option="so2BarOption" height="100%" />
          </div>
        </div>
        <div class="card">
          <div class="card-header">
            <span class="card-title">悬浮颗粒物浓度(PM2.5)超标累计</span>
          </div>
          <div class="card-body">
            <EChart :option="pm25BarOption" height="100%" />
          </div>
        </div>
        <div class="card">
          <div class="card-header">
            <span class="card-title">一氧化碳(CO)浓度超标累计</span>
          </div>
          <div class="card-body">
            <EChart :option="coBarOption" height="100%" />
          </div>
        </div>
      </section>
    </main>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, onBeforeUnmount } from 'vue'
import EChart from '@/components/EChart.vue'
import KpiCounter from '@/components/KpiCounter.vue'
import {
  createBarChart, createPieChart, createLineChart,
  createGaugeChart, createMapChart, ensureMapRegistered
} from '@/components/chartOptions'
import {
  getProvinceExceed, getAqiDistribution, getAqiTrend,
  getGridCoverage, getRealTimeCount
} from '@/api/statistics'

const currentTime = ref('')
const polling = ref(false)
const mapReady = ref(false)
const initialLoading = ref(true)
const consecutiveErrors = ref(0)
const pausedByError = ref(false)
let pollingTimer = null

// 最大连续失败次数，超出自停轮询
const MAX_CONSECUTIVE_ERRORS = 3

const connectionStatus = computed(() => {
  if (pausedByError.value) return 'disconnected'
  if (polling.value) return 'polling'
  return 'idle'
})

const kpi = reactive({ totalCount: 0, goodCount: 0, pollutionCount: 0 })
const rawData = reactive({
  provinceExceed: [],
  aqiDistribution: [],
  aqiTrend: { months: [], exceedCounts: [] },
  gridCoverage: { provinceCoverage: 0, cityCoverage: 0 },
  realTimeCount: { totalCount: 0, goodCount: 0, pollutionCount: 0 }
})

const provinceCoverageOption = computed(() =>
  createGaugeChart('', rawData.gridCoverage.provinceCoverage, 100, '#00e5ff')
)
const cityCoverageOption = computed(() =>
  createGaugeChart('', rawData.gridCoverage.cityCoverage, 100, '#ab47bc')
)
const aqiPieOption = computed(() =>
  createPieChart('', rawData.aqiDistribution)
)
const trendOption = computed(() =>
  createLineChart('', rawData.aqiTrend.months, rawData.aqiTrend.exceedCounts, '超标数', '#ffb300')
)
const mapData = computed(() => {
  return rawData.provinceExceed.map(item => ({
    name: normalizeProvinceName(item.provinceName),
    value: item.aqiExceed
  }))
})

const mapOption = computed(() => {
  return createMapChart('', mapData.value)
})

// 数据库简称 → ECharts 地图全称 归一化
const PROVINCE_NAME_MAP = {
  '内蒙古': '内蒙古自治区',
  '广西': '广西壮族自治区',
  '西藏': '西藏自治区',
  '宁夏': '宁夏回族自治区',
  '新疆': '新疆维吾尔自治区',
  '香港': '香港特别行政区',
  '澳门': '澳门特别行政区'
}
function normalizeProvinceName(name) {
  return PROVINCE_NAME_MAP[name] || name
}

// 车牌简称映射
const PLATE_MAP = {
  '北京市':'京','天津市':'津','上海市':'沪','重庆市':'渝',
  '河北省':'冀','山西省':'晋','辽宁省':'辽','吉林省':'吉','黑龙江省':'黑',
  '江苏省':'苏','浙江省':'浙','安徽省':'皖','福建省':'闽','江西省':'赣','山东省':'鲁',
  '河南省':'豫','湖北省':'鄂','湖南省':'湘','广东省':'粤',
  '广西壮族自治区':'桂','海南省':'琼',
  '四川省':'川','贵州省':'贵','云南省':'云','西藏自治区':'藏',
  '陕西省':'陕','甘肃省':'甘','青海省':'青',
  '宁夏回族自治区':'宁','新疆维吾尔自治区':'新',
  '台湾省':'台','香港特别行政区':'港','澳门特别行政区':'澳',
  '内蒙古自治区':'蒙',
  // 兼容数据库简称
  '内蒙古':'蒙','广西':'桂','西藏':'藏','宁夏':'宁','新疆':'新','香港':'港','澳门':'澳'
}
function plateAbbr(name) {
  return PLATE_MAP[name] || name
}

const pm25BarOption = computed(() => {
  const sorted = [...rawData.provinceExceed]
    .sort((a, b) => b.pm25Exceed - a.pm25Exceed)
    .slice(0, 10)
  return createBarChart('',
    sorted.map(i => i.pm25Exceed),
    sorted.map(i => plateAbbr(i.provinceName)),
    { color: '#00e5ff', rotate: 0 }
  )
})

const so2BarOption = computed(() => {
  const sorted = [...rawData.provinceExceed]
    .sort((a, b) => b.so2Exceed - a.so2Exceed)
    .slice(0, 10)
  return createBarChart('',
    sorted.map(i => i.so2Exceed),
    sorted.map(i => plateAbbr(i.provinceName)),
    { color: '#ffb300', rotate: 0 }
  )
})

const coBarOption = computed(() => {
  const sorted = [...rawData.provinceExceed]
    .sort((a, b) => b.coExceed - a.coExceed)
    .slice(0, 10)
  return createBarChart('',
    sorted.map(i => i.coExceed),
    sorted.map(i => plateAbbr(i.provinceName)),
    { color: '#ab47bc', rotate: 0 }
  )
})

function updateTime() {
  const now = new Date()
  const pad = n => String(n).padStart(2, '0')
  currentTime.value = `${now.getFullYear()}-${pad(now.getMonth() + 1)}-${pad(now.getDate())} ${pad(now.getHours())}:${pad(now.getMinutes())}:${pad(now.getSeconds())}`
}

async function fetchData() {
  if (pausedByError.value) return

  try {
    const [provinceExceed, aqiDistribution, aqiTrend, gridCoverage, realTimeCount] =
      await Promise.all([
        getProvinceExceed(),
        getAqiDistribution(),
        getAqiTrend(),
        getGridCoverage(),
        getRealTimeCount()
      ])

    if (provinceExceed) rawData.provinceExceed = provinceExceed
    if (aqiDistribution) rawData.aqiDistribution = aqiDistribution
    if (aqiTrend) rawData.aqiTrend = aqiTrend
    if (gridCoverage) rawData.gridCoverage = gridCoverage
    if (realTimeCount) {
      rawData.realTimeCount = realTimeCount
      kpi.totalCount = realTimeCount.totalCount
      kpi.goodCount = realTimeCount.goodCount
      kpi.pollutionCount = realTimeCount.pollutionCount
    }

    // 成功后重置
    consecutiveErrors.value = 0
    initialLoading.value = false
  } catch (err) {
    consecutiveErrors.value++
    console.warn(`接口请求失败 (${consecutiveErrors.value}/${MAX_CONSECUTIVE_ERRORS})`, err.message)

    initialLoading.value = false

    // 连续失败达阈值 → 暂停轮询，等待手动重试
    if (consecutiveErrors.value >= MAX_CONSECUTIVE_ERRORS) {
      pausedByError.value = true
      stopPolling()
      console.warn(`已连续 ${MAX_CONSECUTIVE_ERRORS} 次请求失败，自动暂停轮询`)
    }
  }
}


function startPolling() {
  polling.value = true
  fetchData()
  pollingTimer = setInterval(fetchData, 5000)
}

function stopPolling() {
  polling.value = false
  if (pollingTimer) {
    clearInterval(pollingTimer)
    pollingTimer = null
  }
}

/** 手动重试：清错误计数 → 恢复轮询 */
function retryFetch() {
  consecutiveErrors.value = 0
  pausedByError.value = false
  fetchData()
  startPolling()
}

onMounted(async () => {
  updateTime()
  setInterval(updateTime, 1000)

  // 异步加载并注册中国地图 GeoJSON
  const ok = await ensureMapRegistered()
  mapReady.value = ok

  fetchData()
  startPolling()
})

onBeforeUnmount(() => {
  stopPolling()
})
</script>

<style scoped>
.nepv-dashboard {
  width: 100vw;
  height: 100vh;
  position: relative;
  background: #0d1a33;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.dashboard-bg {
  position: absolute;
  inset: 0;
  background:
    radial-gradient(ellipse at 30% 20%, rgba(0, 229, 255, 0.12) 0%, transparent 40%),
    radial-gradient(ellipse at 70% 80%, rgba(123, 31, 162, 0.10) 0%, transparent 40%);
  pointer-events: none;
}

.dashboard-header {
  height: 52px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 24px;
  background: linear-gradient(180deg, rgba(30, 136, 229, 0.22) 0%, rgba(30, 136, 229, 0.04) 100%);
  border-bottom: 1px solid rgba(30, 136, 229, 0.35);
  position: relative;
  z-index: 1;
  flex-shrink: 0;
}

.header-title {
  font-size: 22px;
  font-weight: 700;
  background: linear-gradient(90deg, #00e5ff, #7c4dff, #00e5ff);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  letter-spacing: 3px;
}

.datetime {
  position: absolute;
  left: 24px;
  color: #b0bcc8;
  font-size: 13px;
  font-family: 'DIN', monospace;
}

.dashboard-main {
  flex: 1;
  display: grid;
  grid-template-columns: clamp(340px, 28vw, 560px) 1fr clamp(300px, 22vw, 480px);
  gap: 10px;
  padding: 6px 12px;
  position: relative;
  z-index: 1;
  min-height: 0;
}

.col {
  display: flex;
  flex-direction: column;
  gap: 10px;
  min-height: 0;
}

.col-left {
  display: grid;
  grid-template-rows: 1fr 1fr 1fr;
  gap: 10px;
  min-height: 0;
}

.col-right {
  display: grid;
  grid-template-rows: repeat(3, 1fr);
  gap: 10px;
}

/* 两个仪表图并排一行 */
.gauge-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 10px;
}

.gauge-half {
  min-height: 0;
}

.card-pie, .card-line {
  min-height: 0;
}

.card {
  background: transparent;
  border: none;
  border-radius: 0;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  min-height: 0;
}

.card-header {
  padding: 3px 12px 2px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-bottom: none;
  background: transparent;
  flex-shrink: 0;
}

.card-title {
  font-size: 13px;
  font-weight: 600;
  color: #d0d8e6;
}

.card-body {
  flex: 1;
  padding: 4px 6px;
  min-height: 0;
}

.map-body {
  flex: 1;
  min-height: 0;
}

.col-center {
  display: flex;
  flex-direction: column;
  min-height: 0;
}

.map-card {
  flex: 1;
  min-height: 0;
}

.kpi-bar {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  gap: 10px;
  padding: 4px 0 2px;
  flex-shrink: 0;
}

.kpi-item {
  background: transparent;
  border: none;
  flex-shrink: 0;
}

.kpi-total { border-top: 2px solid #00e5ff; }
.kpi-good { border-top: 2px solid #00e676; }
.kpi-pollution { border-top: 2px solid #ff5252; }

.refresh-indicator {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 12px;
  color: #b0bcc8;
  margin-left: 16px;
}

/* 轮询中 — 绿色 */
.refresh-indicator.polling {
  color: #00e676;
}

/* 连接断开 — 红色闪烁 */
.refresh-indicator.disconnected {
  color: #ff5252;
}

.refresh-indicator .dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #ff5252;
  transition: all 0.3s;
}

.refresh-indicator.polling .dot {
  background: #00e676;
  box-shadow: 0 0 8px #00e676;
  animation: pulse 1.5s infinite;
}

.refresh-indicator.disconnected .dot {
  background: #ff5252;
  box-shadow: 0 0 8px #ff5252;
  animation: blink 1s infinite;
}

@keyframes pulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.4; }
}

@keyframes blink {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.2; }
}

/* 错误横幅 */
.error-banner {
  position: relative;
  z-index: 10;
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 24px;
  background: rgba(255, 82, 82, 0.2);
  border-bottom: 1px solid rgba(255, 82, 82, 0.4);
  color: #ff8a80;
  font-size: 13px;
}

.error-banner .retry-btn {
  margin-left: auto;
  background: rgba(255, 82, 82, 0.3);
  border-color: rgba(255, 82, 82, 0.5);
  color: #fff;
}

.error-banner .retry-btn:hover {
  background: rgba(255, 82, 82, 0.5);
}

.banner-fade-enter-active,
.banner-fade-leave-active {
  transition: all 0.4s ease;
}

.banner-fade-enter-from,
.banner-fade-leave-to {
  opacity: 0;
  transform: translateY(-100%);
}

.map-loading {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  color: #b0bcc8;
  font-size: 14px;
  gap: 12px;
}
</style>
