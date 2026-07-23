import axios from 'axios'
import { showToast } from 'vant'
import { useUserStore } from '../store/user'
import router from '../router'

const request = axios.create({
  baseURL: '/api',
  timeout: 10000,
})

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

request.interceptors.response.use(
  (res) => {
    const data = res.data
    if (data.code === 200) {
      return data
    }
    if (data.code === 401) {
      const userStore = useUserStore()
      userStore.clearUser()
      router.replace('/login')
      showToast(data.msg || '请先登录')
      return Promise.reject(data)
    }
    if (data.code === 403) {
      showToast('无权限')
      return Promise.reject(data)
    }
    showToast(data.msg || '请求失败')
    return Promise.reject(data)
  },
  (error) => {
    showToast(error.message || '网络异常')
    return Promise.reject(error)
  }
)

export default request
