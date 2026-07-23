<template>
  <div class="page">
    <NavBar title="选择网格" right-text="历史" @click-right="$router.push('/history')" />
    <van-cell-group inset>
      <van-field
        v-model="provinceName"
        is-link
        readonly
        label="省份"
        placeholder="请选择省份"
        @click="showProvince = true"
      />
      <van-field
        v-model="cityName"
        is-link
        readonly
        label="城市"
        placeholder="请先选择省份"
        :disabled="!provinceId"
        @click="onOpenCity"
      />
    </van-cell-group>

    <div class="actions">
      <van-button
        round
        block
        type="primary"
        :disabled="!provinceId || !cityId"
        :loading="loading"
        @click="goNext"
      >
        下一步
      </van-button>
    </div>

    <van-popup v-model:show="showProvince" position="bottom" round>
      <van-picker
        :columns="provinceColumns"
        @confirm="onProvinceConfirm"
        @cancel="showProvince = false"
      />
    </van-popup>

    <van-popup v-model:show="showCity" position="bottom" round>
      <van-picker
        :columns="cityColumns"
        @confirm="onCityConfirm"
        @cancel="showCity = false"
      />
    </van-popup>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { showToast } from 'vant'
import NavBar from '../components/NavBar.vue'
import { getCities, getProvinces } from '../api/region'

const router = useRouter()
const loading = ref(false)
const provinces = ref([])
const cities = ref([])

const provinceId = ref(null)
const cityId = ref(null)
const provinceName = ref('')
const cityName = ref('')

const showProvince = ref(false)
const showCity = ref(false)

const provinceColumns = computed(() =>
  provinces.value.map((p) => ({ text: p.provinceName, value: p.id }))
)
const cityColumns = computed(() =>
  cities.value.map((c) => ({ text: c.cityName, value: c.id }))
)

onMounted(async () => {
  loading.value = true
  try {
    const res = await getProvinces()
    provinces.value = res.data || []
  } catch {
    // toast by interceptor
  } finally {
    loading.value = false
  }
})

async function onProvinceConfirm({ selectedOptions }) {
  const opt = selectedOptions[0]
  provinceId.value = opt.value
  provinceName.value = opt.text
  cityId.value = null
  cityName.value = ''
  cities.value = []
  showProvince.value = false
  try {
    const res = await getCities(provinceId.value)
    cities.value = res.data || []
  } catch {
    // ignore
  }
}

function onOpenCity() {
  if (!provinceId.value) {
    showToast('请先选择省份')
    return
  }
  if (!cities.value.length) {
    showToast('该省份暂无城市数据')
    return
  }
  showCity.value = true
}

function onCityConfirm({ selectedOptions }) {
  const opt = selectedOptions[0]
  cityId.value = opt.value
  cityName.value = opt.text
  showCity.value = false
}

function goNext() {
  router.push({
    path: '/feedback',
    query: {
      provinceId: provinceId.value,
      cityId: cityId.value,
      provinceName: provinceName.value,
      cityName: cityName.value,
    },
  })
}
</script>

<style scoped>
.page {
  min-height: 100vh;
  background: #f7f8fa;
}
.actions {
  margin: 24px 16px;
}
</style>
