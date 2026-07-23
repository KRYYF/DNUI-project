<template>
  <div class="page">
    <NavBar title="登录" :show-back="false" />
    <div class="brand">NEPS 公众监督员</div>
    <van-form @submit="onSubmit">
      <van-cell-group inset>
        <van-field
          v-model="form.phone"
          name="phone"
          label="手机号"
          type="tel"
          maxlength="11"
          placeholder="请输入手机号"
          :rules="[
            { required: true, message: '请输入手机号' },
            { pattern: /^1\d{10}$/, message: '手机号格式不正确' },
          ]"
        />
        <van-field
          v-model="form.password"
          name="password"
          label="密码"
          type="password"
          placeholder="请输入密码"
          :rules="[{ required: true, message: '请输入密码' }]"
        />
      </van-cell-group>
      <div class="actions">
        <van-button round block type="primary" native-type="submit" :loading="loading">
          登录
        </van-button>
        <div class="link" @click="$router.push('/register')">没有账号？去注册</div>
      </div>
    </van-form>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { showToast } from 'vant'
import NavBar from '../components/NavBar.vue'
import { login } from '../api/supervisor'
import { useUserStore } from '../store/user'

const router = useRouter()
const userStore = useUserStore()
const loading = ref(false)

const form = reactive({
  phone: '',
  password: '',
})

async function onSubmit() {
  loading.value = true
  try {
    const res = await login({
      phone: form.phone,
      password: form.password,
    })
    userStore.setUser({
      token: res.data.token,
      supervisorId: res.data.supervisorId,
      realName: res.data.realName,
    })
    showToast('登录成功')
    router.replace('/selectGrid')
  } catch {
    // 拦截器已提示后端 msg（如密码错误）
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.page {
  min-height: 100vh;
  background: #f7f8fa;
}
.brand {
  padding: 32px 16px 12px;
  text-align: center;
  font-size: 20px;
  font-weight: 600;
  color: #323233;
}
.actions {
  margin: 24px 16px;
}
.link {
  margin-top: 16px;
  text-align: center;
  color: #1989fa;
  font-size: 14px;
}
</style>
