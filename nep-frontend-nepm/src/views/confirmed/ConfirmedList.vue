<template>
  <div class="page">
    <div class="page-header">
      <h3>确认AQI数据列表</h3>
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
            <el-option v-for="p in provinces" :key="p.id" :label="p.provinceName" :value="p.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="市区域">
          <el-select
            v-model="filter.cityId"
            placeholder="全部城市"
            clearable
            style="width: 140px"
          >
            <el-option v-for="c in cities" :key="c.id" :label="c.cityName" :value="c.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="确认日期">
          <el-date-picker
            v-model="filter.confirmDate"
            type="date"
            placeholder="选择日期"
            value-format="YYYY-MM-DD"
            clearable
            style="width: 150px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSearch">查询</el-button>
          <el-button @click="onClear">清空</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 表格 -->
    <el-card class="table-card" shadow="never">
      <el-table :data="tableData" v-loading="loading" stripe border style="width: 100%">
        <el-table-column prop="id" label="编号" width="70" />
        <el-table-column prop="provinceName" label="省份" width="100" />
        <el-table-column prop="cityName" label="城市" width="100" />
        <el-table-column prop="address" label="地址" min-width="150" show-overflow-tooltip />
        <el-table-column prop="so2Level" label="SO₂" width="70">
          <template #default="{ row }">
            <el-tag :type="row.so2Level > 2 ? 'danger' : 'success'" size="small">
              {{ row.so2Level ?? '-' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="coLevel" label="CO" width="70">
          <template #default="{ row }">
            <el-tag :type="row.coLevel > 2 ? 'danger' : 'success'" size="small">
              {{ row.coLevel ?? '-' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="spmLevel" label="PM2.5" width="80">
          <template #default="{ row }">
            <el-tag :type="row.spmLevel > 2 ? 'danger' : 'success'" size="small">
              {{ row.spmLevel ?? '-' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="aqiLevel" label="AQI等级" width="100">
          <template #default="{ row }">
            <el-tag :type="levelTagType(row.aqiLevel)" size="small">
              {{ levelText(row.aqiLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="gridMemberName" label="检测网格员" width="110" />
        <el-table-column prop="confirmDate" label="确认日期" width="120" />
        <el-table-column label="操作" width="100" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="goDetail(row.id)">
              查看详情
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
import { confirmedPageQuery } from '../../api/statistics'
import { getProvinces, getCities } from '../../api/region'

const router = useRouter()
const loading = ref(false)
const tableData = ref([])
const provinces = ref([])
const cities = ref([])

const filter = reactive({
  provinceId: undefined,
  cityId: undefined,
  confirmDate: undefined,
})

const pager = reactive({ current: 1, size: 10, total: 0 })

const levelMap = { 1: '优', 2: '良', 3: '轻度污染', 4: '中度污染', 5: '重度污染', 6: '严重污染' }
function levelText(val) { return levelMap[val] || val }
function levelTagType(val) {
  if (val <= 2) return 'success'
  if (val <= 3) return 'warning'
  return 'danger'
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
    const params = { page: pager.current, size: pager.size }
    if (filter.provinceId) params.provinceId = filter.provinceId
    if (filter.cityId) params.cityId = filter.cityId
    if (filter.confirmDate) params.confirmDate = filter.confirmDate
    const res = await confirmedPageQuery(params)
    tableData.value = res.data.records
    pager.total = res.data.total
  } finally {
    loading.value = false
  }
}

function onClear() {
  filter.provinceId = undefined
  filter.cityId = undefined
  filter.confirmDate = undefined
  cities.value = []
  pager.current = 1
  onSearch()
}

function goDetail(id) { router.push(`/confirmed/detail/${id}`) }
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
.table-card {
  border-radius: 14px;
  border: 1px solid #eef3ee;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.03);
}
.pagination-wrap { margin-top: 16px; display: flex; justify-content: flex-end; }
</style>
