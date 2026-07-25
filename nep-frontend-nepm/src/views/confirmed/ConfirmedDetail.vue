<template>
  <div class="page">
    <div class="page-header">
      <el-button :icon="ArrowLeft" @click="router.back()">返回</el-button>
      <h3>确认AQI数据详情</h3>
    </div>

    <el-card v-loading="loading" shadow="never">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="数据编号">{{ detail.id }}</el-descriptions-item>
        <el-descriptions-item label="确认日期">{{ detail.confirmDate }}</el-descriptions-item>
        <el-descriptions-item label="省区域">{{ detail.provinceName }}</el-descriptions-item>
        <el-descriptions-item label="市区域">{{ detail.cityName }}</el-descriptions-item>
        <el-descriptions-item label="详细地址" :span="2">{{ detail.address }}</el-descriptions-item>
        <el-descriptions-item label="检测网格员">{{ detail.gridMemberName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="确认时间">{{ detail.confirmTime || '-' }}</el-descriptions-item>

        <el-descriptions-item label="SO₂浓度值">{{ detail.so2Value ?? '-' }}</el-descriptions-item>
        <el-descriptions-item label="SO₂ AQI等级">
          <el-tag :type="detail.so2Level > 2 ? 'danger' : 'success'" size="small">
            {{ detail.so2Level }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="CO浓度值">{{ detail.coValue ?? '-' }}</el-descriptions-item>
        <el-descriptions-item label="CO AQI等级">
          <el-tag :type="detail.coLevel > 2 ? 'danger' : 'success'" size="small">
            {{ detail.coLevel }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="PM2.5浓度值">{{ detail.spmValue ?? '-' }}</el-descriptions-item>
        <el-descriptions-item label="PM2.5 AQI等级">
          <el-tag :type="detail.spmLevel > 2 ? 'danger' : 'success'" size="small">
            {{ detail.spmLevel }}
          </el-tag>
        </el-descriptions-item>

        <el-descriptions-item label="实测AQI等级" :span="2">
          <el-tag :type="levelTagType(detail.aqiLevel)" size="large">
            {{ levelText(detail.aqiLevel) }}
          </el-tag>
        </el-descriptions-item>
      </el-descriptions>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ArrowLeft } from '@element-plus/icons-vue'
import { getConfirmedDetail } from '../../api/statistics'

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const detail = ref({})

const levelMap = { 1: '优', 2: '良', 3: '轻度污染', 4: '中度污染', 5: '重度污染', 6: '严重污染' }
function levelText(val) { return levelMap[val] || val }
function levelTagType(val) {
  if (val <= 2) return 'success'
  if (val <= 3) return 'warning'
  return 'danger'
}

onMounted(async () => {
  loading.value = true
  try {
    const res = await getConfirmedDetail(route.params.id)
    detail.value = res.data
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.page { max-width: 900px; }
.page-header {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 20px;
}
.page-header h3 {
  margin: 0;
  font-size: 20px;
  font-weight: 700;
  color: #1a1a2e;
  letter-spacing: 1px;
}
.page-header :deep(.el-button) {
  border-radius: 10px;
  transition: all 0.3s ease;
}
.page-header :deep(.el-button:hover) {
  border-color: #43A047;
  color: #43A047;
}
.page :deep(.el-card) {
  border-radius: 14px;
  border: 1px solid #eef3ee;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.04);
}
</style>
