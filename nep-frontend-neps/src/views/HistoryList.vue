<template>
  <div class="page">
    <NavBar title="历史反馈" back-to="/selectGrid" />

    <van-pull-refresh v-model="refreshing" @refresh="loadData">
      <van-empty v-if="!loading && !list.length" description="暂无反馈记录" />
      <van-cell-group v-else inset>
        <van-cell
          v-for="item in list"
          :key="item.id"
          :title="`${item.provinceName || ''}-${item.cityName || ''}`"
          :label="formatDesc(item)"
          :value="formatTime(item.feedbackTime)"
        >
          <template #icon>
            <span
              class="level-tag"
              :style="{ background: getAqiInfo(item.estimatedLevel).color }"
            >
              {{ getAqiInfo(item.estimatedLevel).grade }}
            </span>
          </template>
        </van-cell>
      </van-cell-group>
    </van-pull-refresh>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { showToast } from 'vant'
import NavBar from '../components/NavBar.vue'
import { getMyFeedbackList } from '../api/feedback'
import { useUserStore } from '../store/user'
import { getAqiInfo } from '../utils/aqi'

const router = useRouter()
const userStore = useUserStore()
const list = ref([])
const loading = ref(false)
const refreshing = ref(false)

onMounted(() => {
  loadData()
})

async function loadData() {
  if (!userStore.supervisorId) {
    showToast('请先登录')
    router.replace('/login')
    return
  }
  loading.value = true
  try {
    const res = await getMyFeedbackList(userStore.supervisorId)
    list.value = res.data || []
  } catch {
    // interceptor
  } finally {
    loading.value = false
    refreshing.value = false
  }
}

function formatDesc(item) {
  const desc = item.feedbackDesc || ''
  const short = desc.length > 30 ? `${desc.slice(0, 30)}...` : desc
  return `${item.status || ''} · ${short}`
}

function formatTime(t) {
  if (!t) return ''
  return String(t).replace('T', ' ').slice(0, 16)
}
</script>

<style scoped>
.page {
  min-height: 100vh;
  background: #f7f8fa;
  padding-bottom: 16px;
}
.level-tag {
  display: inline-block;
  min-width: 48px;
  margin-right: 8px;
  padding: 2px 6px;
  border-radius: 4px;
  color: #111;
  font-size: 12px;
  text-align: center;
}
</style>
