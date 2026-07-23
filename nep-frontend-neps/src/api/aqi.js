import request from '../utils/request'

export function getAqiLevels() {
  return request.get('/aqi/levels')
}
