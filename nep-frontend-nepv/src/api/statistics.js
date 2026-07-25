import request from '@/utils/request'

export function getProvinceExceed() {
  return request({
    url: '/statistics/provinceExceed',
    method: 'get'
  })
}

export function getAqiDistribution() {
  return request({
    url: '/statistics/aqiDistribution',
    method: 'get'
  })
}

export function getAqiTrend() {
  return request({
    url: '/statistics/aqiTrend',
    method: 'get'
  })
}

export function getGridCoverage() {
  return request({
    url: '/statistics/gridCoverage',
    method: 'get'
  })
}

export function getRealTimeCount() {
  return request({
    url: '/statistics/realTimeCount',
    method: 'get'
  })
}
