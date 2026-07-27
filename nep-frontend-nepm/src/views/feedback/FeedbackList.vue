<template>
  <div class="page">
    <div class="page-header">
      <h3>公众监督数据列表</h3>
    </div>

    <!-- 筛选区 -->
    <el-card class="filter-card" shadow="never">
      <el-form :model="filter" inline>
        <el-form-item label="省区域">
          <el-select
            v-model="filter.provinceId"
            placeholder="全部省份"
            clearable
            @change="onProvinceChange"
            style="width: 140px"
          >
            <el-option
              v-for="p in provinces"
              :key="p.id"
              :label="p.provinceName"
              :value="p.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="市区域">
          <el-select
            v-model="filter.cityId"
            placeholder="全部城市"
            clearable
            style="width: 140px"
          >
            <el-option
              v-for="c in cities"
              :key="c.id"
              :label="c.cityName"
              :value="c.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="预估等级">
          <el-select v-model="filter.aqiLevel" placeholder="全部等级" clearable style="width: 120px">
            <el-option label="优" :value="1" />
            <el-option label="良" :value="2" />
            <el-option label="轻度污染" :value="3" />
            <el-option label="中度污染" :value="4" />
            <el-option label="重度污染" :value="5" />
            <el-option label="严重污染" :value="6" />
          </el-select>
        </el-form-item>
        <el-form-item label="反馈日期">
          <el-date-picker
            v-model="filter.afDate"
            type="date"
            placeholder="选择日期"
            value-format="YYYY-MM-DD"
            clearable
            style="width: 150px"
          />
        </el-form-item>
        <el-form-item label="指派状态">
          <el-radio-group v-model="filter.state" @change="onSearch">
            <el-radio-button value="">全部</el-radio-button>
            <el-radio-button :value="0">未指派</el-radio-button>
            <el-radio-button :value="1">已指派</el-radio-button>
            <el-radio-button :value="2">已确认</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSearch">查询</el-button>
          <el-button @click="onClear">清空</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="table-card" shadow="never">
      <el-table :data="tableData" v-loading="loading" stripe border style="width: 100%">
        <el-table-column prop="id" label="编号" width="70" />
        <el-table-column prop="provinceName" label="省份" width="100" />
        <el-table-column prop="cityName" label="城市" width="100" />
        <el-table-column prop="detailAddress" label="地址" min-width="150" show-overflow-tooltip />
        <el-table-column prop="estimatedLevel" label="预估AQI" width="100">
          <template #default="{ row }">
            <el-tag :type="levelTagType(row.estimatedLevel)" size="small">
              {{ levelText(row.estimatedLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="feedbackDesc" label="描述" width="180" show-overflow-tooltip />
        <el-table-column prop="feedbackTime" label="反馈时间" width="160" />
        <el-table-column prop="status" label="状态" width="90">
          <template #default="{ row }">
            <el-tag :type="statusTagType(row.status)" size="small">{{ row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="goDetail(row.id)">
              查看详情
            </el-button>
            <el-button
              v-if="row.status === '未指派'"
              type="success"
              link
              size="small"
              @click="goAssign(row.id)"
            >
              指派网格员
            </el-button>
            <el-button
              v-if="row.status === '已指派'"
              type="warning"
              link
              size="small"
              @click="goAssign(row.id)"
            >
              重新指派
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrap">
        <el-pagination
          v-model:current-page="pager.current"
          v-model:page-size="pager.size"
          :total="pager.total"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next"
          @size-change="onSearch"
          @current-change="onSearch"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { pageQuery } from '../../api/feedback'
import { getProvinces, getCities } from '../../api/region'

const router = useRouter()

const loading = ref(false)
const tableData = ref([])
const provinces = ref([])
const cities = ref([])

const filter = reactive({
  provinceId: undefined,
  cityId: undefined,
  aqiLevel: undefined,
  afDate: undefined,
  state: '',
})

const pager = reactive({ current: 1, size: 10, total: 0 })

const levelMap = { 1: '优', 2: '良', 3: '轻度污染', 4: '中度污染', 5: '重度污染', 6: '严重污染' }
function levelText(val) { return levelMap[val] || val }
function levelTagType(val) {
  if (val <= 2) return 'success'
  if (val <= 3) return 'warning'
  return 'danger'
}
function statusTagType(status) {
  if (status === '未指派') return 'info'
  if (status === '已指派') return 'warning'
  return 'success'
}

onMounted(async () => {
  const res = await getProvinces()
  provinces.value = res.data
  onSearch()
})

async function onProvinceChange(val) {
  filter.cityId = undefined
  cities.value = []
  if (val) {
    const res = await getCities(val)
    cities.value = res.data
  }
  onSearch()
}

async function onSearch() {
  loading.value = true
  try {
    const params = {
      page: pager.current,
      size: pager.size,
    }
    if (filter.provinceId) params.provinceId = filter.provinceId
    if (filter.cityId) params.cityId = filter.cityId
    if (filter.aqiLevel) params.aqiLevel = filter.aqiLevel
    if (filter.afDate) params.afDate = filter.afDate
    if (filter.state !== undefined && filter.state !== null && filter.state !== '') {
      params.state = filter.state
    }
    const res = await pageQuery(params)
    tableData.value = res.data.records
    pager.total = res.data.total
  } catch {
    // handled by interceptor
  } finally {
    loading.value = false
  }
}

function onClear() {
  filter.provinceId = undefined
  filter.cityId = undefined
  filter.aqiLevel = undefined
  filter.afDate = undefined
  filter.state = ''
  cities.value = []
  pager.current = 1
  onSearch()
}

function goDetail(id) { router.push(`/feedback/detail/${id}`) }
function goAssign(id) { router.push(`/feedback/assign/${id}`) }
</script>

<style scoped>
.page { max-width: 1400px; }
.page-header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}
.page-header h3 {
  margin: 0;
  font-size: 20px;
  font-weight: 700;
  color: #1a1a2e;
  letter-spacing: 1px;
}
.filter-card {
  margin-bottom: 16px;
  border-radius: 14px;
  border: 1px solid #eef3ee;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.03);
}
.filter-card :deep(.el-card__body) {
  padding: 20px 24px 8px;
}
.filter-card :deep(.el-button--primary) {
  background: linear-gradient(135deg, #43A047 0%, #2E7D32 50%, #1B5E20 100%);
  border: none;
  border-radius: 10px;
  font-weight: 500;
  transition: all 0.3s ease;
}
.filter-card :deep(.el-button--primary:hover) {
  box-shadow: 0 4px 14px rgba(67, 160, 71, 0.35);
  transform: translateY(-1px);
}
.filter-card :deep(.el-select .el-input__wrapper) {
  border-radius: 10px;
}
.filter-card :deep(.el-input__wrapper) {
  border-radius: 10px;
}
.filter-card :deep(.el-radio-button__inner) {
  border-radius: 8px;
}
.table-card {
  border-radius: 14px;
  border: 1px solid #eef3ee;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.03);
}
.pagination-wrap { margin-top: 16px; display: flex; justify-content: flex-end; }
</style>
