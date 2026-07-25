import request from '../utils/request'

export function pageQuery(params) {
  return request.get('/aqiFeedback/pageQuery', { params })
}

export function getFeedbackDetail(id) {
  return request.get(`/aqiFeedback/detail/${id}`)
}

export function assignFeedback(feedbackId, gmId) {
  return request.post('/aqiFeedback/assign', { feedbackId, gmId })
}
