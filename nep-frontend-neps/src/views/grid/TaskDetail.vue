<template>
  <div class="container">
    <h2>任务详情与监测填报</h2>
    <div class="info">
      <p>地址：{{detail.address}}</p>
      <p>反馈内容：{{detail.information}}</p>
      <p>预估等级：{{detail.estimatedGrade}}</p>
    </div>
    <div>
      <label>SO₂数值：</label>
      <input v-model.number="form.so2Value" type="number"/>
    </div>
    <div>
      <label>CO数值：</label>
      <input v-model.number="form.coValue" type="number"/>
    </div>
    <div>
      <label>PM2.5数值：</label>
      <input v-model.number="form.pm25Value" type="number"/>
    </div>
    <button @click="submitData">提交监测数据</button>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getTaskDetail, submitDetect } from '../../api/gridApi.js'

const route = useRoute()
const router = useRouter()
const afId = route.query.afId
const gmId = localStorage.getItem("gmId")

const detail = ref({})
const form = ref({
  afId: afId,
  gmId: gmId,
  so2Value: null,
  coValue: null,
  pm25Value: null
})

async function loadDetail() {
  const res = await getTaskDetail(afId)
  const json = await res.json()
  if (json.code === 200) {
    detail.value = json.data
  }
}

async function submitData() {
  if (!form.value.so2Value || !form.value.coValue || !form.value.pm25Value) {
    alert("请填写全部监测数值")
    return
  }
  const res = await submitDetect(form.value)
  const json = await res.json()
  if (json.code === 200) {
    alert("提交成功！")
    router.push("/taskList")
  } else {
    alert(json.msg || "提交失败")
  }
}

onMounted(loadDetail)
</script>

<style scoped>
.container {
  width: 92%;
  margin: 0 auto;
}
.info {
  border: 1px solid #ddd;
  padding: 10px;
  border-radius: 6px;
  margin-bottom: 16px;
}
input {
  margin-left: 8px;
  padding: 4px;
}
button {
  margin-top: 14px;
  padding: 8px 16px;
  background: #2d8cf0;
  color: #fff;
  border: none;
  border-radius: 4px;
}
</style>
