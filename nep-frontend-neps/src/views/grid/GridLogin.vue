<template>
  <div class="login-box">
    <h2>网格员登录</h2>
    <div class="item">
      <label>网格员编码(gmCode)</label>
      <input v-model="form.gmCode" placeholder="请输入编码"/>
    </div>
    <div class="item">
      <label>密码(password)</label>
      <input v-model="form.password" type="password" placeholder="请输入密码"/>
    </div>
    <button @click="login">登录</button>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
// 路径适配 views/grid 目录
import { gridLogin } from '../../api/gridApi.js'

const router = useRouter()
const form = ref({
  gmCode: '',
  password: ''
})

async function login() {
  if (!form.value.gmCode || !form.value.password) {
    alert("请填写完整信息")
    return
  }
  const res = await gridLogin(form.value)
  const json = await res.json()
  if (json.code === 200) {
    localStorage.setItem("gmId", json.data.gmId)
    alert("登录成功")
    router.push("/taskList")
  } else {
    alert(json.msg || "登录失败")
  }
}
</script>

<style scoped>
.login-box {
  width: 420px;
  margin: 120px auto;
}
.item {
  margin: 12px 0;
}
input {
  width: 100%;
  padding: 6px;
  margin: 4px 0;
}
button {
  width: 100%;
  padding: 10px;
  background: #2d8cf0;
  color: #fff;
  border: none;
  border-radius: 4px;
}
</style>
