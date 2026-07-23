<template>
  <div class="page">
    <NavBar title="注册" :show-back="false" />
    <van-form @submit="onSubmit">
      <van-cell-group inset>
        <van-field
          v-model="form.phone"
          name="phone"
          label="手机号"
          type="tel"
          maxlength="11"
          placeholder="请输入11位手机号"
          :rules="[
            { required: true, message: '请输入手机号' },
            { pattern: /^1\d{10}$/, message: '手机号格式不正确' },
          ]"
          @blur="onPhoneBlur"
        />
        <van-field
          v-if="phoneTip"
          :model-value="phoneTip"
          readonly
          :error="phoneExists"
          label=" "
        />
        <van-field
          v-model="form.realName"
          name="realName"
          label="真实姓名"
          placeholder="请输入真实姓名"
          :rules="[{ required: true, message: '请输入真实姓名' }]"
        />
        <van-field
          v-model="form.birthDate"
          name="birthDate"
          label="出生日期"
          type="date"
          placeholder="请选择出生日期"
        />
        <van-field name="gender" label="性别">
          <template #input>
            <van-radio-group v-model="form.gender" direction="horizontal">
              <van-radio name="男">男</van-radio>
              <van-radio name="女">女</van-radio>
            </van-radio-group>
          </template>
        </van-field>
        <van-field
          v-model="form.password"
          name="password"
          label="密码"
          type="password"
          placeholder="6-20位密码"
          :rules="[
            { required: true, message: '请输入密码' },
            { pattern: /^.{6,20}$/, message: '密码需为6-20位' },
          ]"
        />
        <van-field
          v-model="form.confirmPassword"
          name="confirmPassword"
          label="确认密码"
          type="password"
          placeholder="再次输入密码"
          :rules="[
            { required: true, message: '请确认密码' },
            { validator: validateConfirm, message: '两次密码不一致' },
          ]"
        />
      </van-cell-group>

      <div class="actions">
        <van-button round block type="primary" native-type="submit" :loading="loading">
          注册
        </van-button>
        <div class="link" @click="$router.push('/login')">已有账号？去登录</div>
      </div>
    </van-form>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { showToast } from 'vant'
import NavBar from '../components/NavBar.vue'
import { checkPhone, register } from '../api/supervisor'

const router = useRouter()
const loading = ref(false)
const phoneExists = ref(false)
const phoneTip = ref('')

const form = reactive({
  phone: '',
  realName: '',
  birthDate: '',
  gender: '男',
  password: '',
  confirmPassword: '',
})

function validateConfirm(val) {
  return val === form.password
}

async function onPhoneBlur() {
  phoneTip.value = ''
  phoneExists.value = false
  if (!/^1\d{10}$/.test(form.phone)) return
  try {
    const res = await checkPhone(form.phone)
    if (res.data?.exists) {
      phoneExists.value = true
      phoneTip.value = '该手机号已注册'
    } else {
      phoneTip.value = '手机号可用'
    }
  } catch {
    // 拦截器已提示
  }
}

async function onSubmit() {
  if (phoneExists.value) {
    showToast('该手机号已注册')
    return
  }
  loading.value = true
  try {
    await register({
      phone: form.phone,
      password: form.password,
      realName: form.realName,
      birthDate: form.birthDate || null,
      gender: form.gender,
    })
    showToast('注册成功')
    router.replace('/login')
  } catch {
    // 拦截器已提示
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.page {
  min-height: 100vh;
  background: #f7f8fa;
  padding-bottom: 24px;
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
