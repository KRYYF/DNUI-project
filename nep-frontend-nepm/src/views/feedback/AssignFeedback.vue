<template>
  <div class="page">
    <div class="page-header">
      <el-button :icon="ArrowLeft" @click="router.back()">返回</el-button>
      <h3>指派网格员</h3>
    </div>

    <!-- 反馈摘要 -->
    <el-card shadow="never" class="summary-card">
      <el-descriptions :column="2" border size="small">
        <el-descriptions-item label="反馈编号">{{ feedback.id }}</el-descriptions-item>
        <el-descriptions-item label="反馈时间">{{ feedback.feedbackTime }}</el-descriptions-item>
        <el-descriptions-item label="省区域">{{ feedback.provinceName }}</el-descriptions-item>
        <el-descriptions-item label="市区域">{{ feedback.cityName }}</el-descriptions-item>
        <el-descriptions-item label="详细地址" :span="2">{{ feedback.address }}</el-descriptions-item>
        <el-descriptions-item label="预估AQI">
          <el-tag :type="levelTagType(feedback.estimatedLevel)" size="small">
            {{ levelText(feedback.estimatedLevel) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="当前指派">{{ feedback.gridMemberName || '未指派' }}</el-descriptions-item>
      </el-descriptions>
    </el-card>

    <!-- 指派类型切换 -->
    <el-card shadow="never" class="assign-card">
      <div class="assign-type-toggle">
        <span class="toggle-label">指派方式：</span>
        <el-radio-group v-model="isRemote" @change="onRemoteChange">
          <el-radio-button :value="false">本地指派</el-radio-button>
          <el-radio-button :value="true">异地指派</el-radio-button>
        </el-radio-group>
      </div>

      <!-- 本地指派 -->
      <div v-if="!isRemote" class="assign-form">
        <el-form label-width="100px">
          <el-form-item label="选择网格员">
            <el-select v-model="selectedGmId" placeholder="请选择本地网格员" style="width: 300px">
              <el-option
                v-for="gm in localMembers"
                :key="gm.gmId"
                :label="`${gm.gmName} (${gm.tel || '无电话'})`"
                :value="gm.gmId"
              />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :loading="assigning" :disabled="!selectedGmId" @click="doAssign">
              本 地 指 派
            </el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 异地指派 -->
      <div v-else class="assign-form">
        <el-form label-width="100px">
          <el-form-item label="选择省份">
            <el-select
              v-model="remoteProvinceId"
              placeholder="请选择省份"
              @change="onRemoteProvinceChange"
              style="width: 200px"
            >
              <el-option
                v-for="p in provinces"
                :key="p.id"
                :label="p.provinceName"
                :value="p.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="选择城市">
            <el-select
              v-model="remoteCityId"
              placeholder="请选择城市"
              @change="onRemoteCityChange"
              style="width: 200px"
            >
              <el-option
                v-for="c in remoteCities"
                :key="c.id"
                :label="c.cityName"
                :value="c.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="选择网格员">
            <el-select v-model="selectedGmId" placeholder="请选择网格员" style="width: 300px">
              <el-option
                v-for="gm in remoteMembers"
                :key="gm.gmId"
                :label="`${gm.gmName} (${gm.tel || '无电话'})`"
                :value="gm.gmId"
              />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :loading="assigning" :disabled="!selectedGmId" @click="doAssign">
              异 地 指 派
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ArrowLeft } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { getFeedbackDetail, assignFeedback } from '../../api/feedback'
import { getGridMembersByRegion } from '../../api/gridMember'
import { getProvinces, getCities } from '../../api/region'

const route = useRoute()
const router = useRouter()

const feedback = ref({})
const assigning = ref(false)
const isRemote = ref(false)
const selectedGmId = ref(null)

// 本地
const localMembers = ref([])

// 异地
const provinces = ref([])
const remoteProvinceId = ref(null)
const remoteCities = ref([])
const remoteCityId = ref(null)
const remoteMembers = ref([])

const levelMap = { 1: '优', 2: '良', 3: '轻度污染', 4: '中度污染', 5: '重度污染', 6: '严重污染' }
function levelText(val) { return levelMap[val] || val }
function levelTagType(val) {
  if (val <= 2) return 'success'
  if (val <= 3) return 'warning'
  return 'danger'
}

onMounted(async () => {
  const res = await getFeedbackDetail(route.params.id)
  feedback.value = res.data
  // 加载本地网格员
  loadLocalMembers()
  // 预加载省份（异地用）
  const pRes = await getProvinces()
  provinces.value = pRes.data
})

async function loadLocalMembers() {
  const res = await getGridMembersByRegion(feedback.value.provinceId, feedback.value.cityId)
  localMembers.value = res.data
}

async function onRemoteProvinceChange(val) {
  remoteCityId.value = null
  remoteMembers.value = []
  if (val) {
    const res = await getCities(val)
    remoteCities.value = res.data
  } else {
    remoteCities.value = []
  }
}

async function onRemoteCityChange(val) {
  remoteMembers.value = []
  if (val && remoteProvinceId.value) {
    const res = await getGridMembersByRegion(remoteProvinceId.value, val)
    remoteMembers.value = res.data
  }
}

function onRemoteChange(val) {
  selectedGmId.value = null
  if (!val) {
    remoteProvinceId.value = null
    remoteCities.value = []
    remoteCityId.value = null
    remoteMembers.value = []
  }
}

async function doAssign() {
  assigning.value = true
  try {
    await assignFeedback(feedback.value.id, selectedGmId.value)
    ElMessage.success('指派成功')
    router.push('/feedback/list')
  } catch {
    // handled by interceptor
  } finally {
    assigning.value = false
  }
}
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
.summary-card {
  margin-bottom: 16px;
  border-radius: 14px;
  border: 1px solid #eef3ee;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.04);
}
.assign-card {
  margin-bottom: 16px;
  border-radius: 14px;
  border: 1px solid #eef3ee;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.04);
}
.assign-type-toggle { display: flex; align-items: center; margin-bottom: 20px; }
.toggle-label { margin-right: 12px; font-weight: 600; color: #1a1a2e; }
.assign-form { padding-left: 0; }
.assign-card :deep(.el-button--primary) {
  background: linear-gradient(135deg, #43A047 0%, #2E7D32 50%, #1B5E20 100%);
  border: none;
  border-radius: 10px;
  font-weight: 600;
  letter-spacing: 4px;
  transition: all 0.3s ease;
}
.assign-card :deep(.el-button--primary:hover) {
  box-shadow: 0 4px 14px rgba(67, 160, 71, 0.35);
  transform: translateY(-1px);
}
.assign-card :deep(.el-select .el-input__wrapper) {
  border-radius: 10px;
}
</style>
