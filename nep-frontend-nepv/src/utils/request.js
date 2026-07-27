import axios from 'axios'

const service = axios.create({
  baseURL: '/api',
  timeout: 10000
})

service.interceptors.request.use(
  config => {
    const token = localStorage.getItem('nepv_token')
    if (token) {
      config.headers['Authorization'] = 'Bearer ' + token
    }
    return config
  },
  error => Promise.reject(error)
)

service.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code === 200) {
      return res.data
    } else if (res.code === 401) {
      localStorage.removeItem('nepv_token')
      localStorage.removeItem('nepv_userInfo')
      window.location.hash = '#/login'
      return Promise.reject(new Error('未授权'))
    } else {
      return Promise.reject(new Error(res.msg || '请求失败'))
    }
  },
  error => {
    if (error.response && error.response.status === 401) {
      localStorage.removeItem('nepv_token')
      localStorage.removeItem('nepv_userInfo')
      window.location.hash = '#/login'
    }
    return Promise.reject(error)
  }
)

export default service
