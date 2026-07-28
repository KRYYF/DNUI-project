import request from '../utils/request'


// 网格员登录
export function gridMemberLogin(data) {
  return request.post('/gridMember/login', data)
}


// 获取网格员任务列表
export function getTasks() {
  return request.get('/gridMember/tasks')
}


// 获取任务详情
export function getTaskDetail(afId) {
  return request.get(`/gridMember/detail/${afId}`)
}


// 提交AQI数据
export function submitAqi(data) {
  return request.post('/gridMember/submit', data)
}