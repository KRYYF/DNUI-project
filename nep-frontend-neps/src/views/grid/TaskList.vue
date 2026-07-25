<template>
  <div class="container">
    <h2>我的监测任务</h2>
    <div v-for="item in list" :key="item.afId" class="card">
      <p>地址：{{item.address}}</p>
      <p>反馈信息：{{item.information}}</p>
      <p>预估等级：{{item.estimatedGrade}}</p>
      <button @click="goDetail(item.afId)">前往填报监测数据</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getTaskList } from '../../api/gridApi.js'

const router = useRouter()
const list = ref([])
const gmId = localStorage.getItem("gmId")

async function load() {
  const res = await getTaskList(gmId, 1, 10)
  const json = await res.json()
  if (json.code === 200) {
    list.value = json.data.records
  }
}

function goDetail(afId) {
  router.push(`/taskDetail?afId=${afId}`)
}

onMounted(load)
</script>

<style scoped>
.container {
  width: 92%;
  margin: 0 auto;
}
.card {
  border: 1px solid #eee;
  padding: 12px;
  border-radius: 6px;
  margin: 8px 0;
}
button {
  padding: 6px 14px;
  background: #009966;
  color: #fff;
  border: none;
  border-radius: 4px;
}
</style>
