import request from '../utils/request'

export function register(data) {
  return request.post('/supervisor/register', data)
}

export function checkPhone(phone) {
  return request.get('/supervisor/checkPhone', { params: { phone } })
}

export function login(data) {
  return request.post('/supervisor/login', data)
}
