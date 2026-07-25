import axios from 'axios'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../store/user'
import router from '../router'

const request = axios.create({
  baseURL: '/api',
  timeout: 10000,
})

// 请求拦截器 — 附加 token
request.interceptors.request.use(
  (config) => {
    const userStore = useUserStore()
    if (userStore.token) {
      config.headers.Authorization = userStore.token
    }
    return config
  },
  (error) => Promise.reject(error)
)

// 响应拦截器 — 统一处理业务码
request.interceptors.response.use(
  (response) => {
    const data = response.data
    if (data.code === 200) {
      return data
    }
    if (data.code === 401) {
      const userStore = useUserStore()
      userStore.clearUser()
      router.push('/login')
      ElMessage.error('登录已过期，请重新登录')
      return Promise.reject(data)
    }
    if (data.code === 403) {
      ElMessage.error('无权限')
      return Promise.reject(data)
    }
    ElMessage.error(data.msg || '请求失败')
    return Promise.reject(data)
  },
  (error) => {
    ElMessage.error(error.message || '网络异常')
    return Promise.reject(error)
  }
)

export default request
