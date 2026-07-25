import request from '../utils/request'

export function getProvinces() {
  return request.get('/region/provinces')
}

export function getCities(provinceId) {
  return request.get(`/region/cities/${provinceId}`)
}
