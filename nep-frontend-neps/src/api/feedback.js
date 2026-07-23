import request from '../utils/request'

export function submitFeedback(data) {
  return request.post('/aqiFeedback/submit', data)
}

export function getMyFeedbackList(supervisorId) {
  return request.get('/aqiFeedback/myList', { params: { supervisorId } })
}
