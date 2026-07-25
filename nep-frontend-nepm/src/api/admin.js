import request from '../utils/request'

export function adminLogin(adminCode, password) {
  return request.post('/admin/login', { adminCode, password })
}
