import request from '../utils/request'

export function getProvinceExceed() {
  return request.get('/statistics/provinceExceed')
}

export function getAqiDistribution() {
  return request.get('/statistics/aqiDistribution')
}

export function getAqiTrend() {
  return request.get('/statistics/aqiTrend')
}

export function getGridCoverage() {
  return request.get('/statistics/gridCoverage')
}

export function getRealTimeCount() {
  return request.get('/statistics/realTimeCount')
}

export function confirmedPageQuery(params) {
  return request.get('/statistics/confirmedPageQuery', { params })
}

export function getConfirmedDetail(id) {
  return request.get(`/statistics/confirmedDetail/${id}`)
}
