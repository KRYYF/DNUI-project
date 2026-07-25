import request from '../utils/request'

export function getGridMembersByRegion(provinceId, cityId) {
  return request.get('/gridMember/byRegion', { params: { provinceId, cityId } })
}

export function getAllGridMembers() {
  return request.get('/gridMember/list')
}

export function getGridMemberById(gmId) {
  return request.get(`/gridMember/${gmId}`)
}
