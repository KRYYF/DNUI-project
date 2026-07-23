/** AQI 等级兜底数据（后端 /aqi/levels 不通时使用） */
export const AQI_LEVELS = [
  { level: 1, grade: '优', color: '#00e400', description: '空气质量令人满意' },
  { level: 2, grade: '良', color: '#ffff00', description: '空气质量可接受' },
  { level: 3, grade: '轻度污染', color: '#ff7e00', description: '敏感人群有影响' },
  { level: 4, grade: '中度污染', color: '#ff0000', description: '对所有人群不健康' },
  { level: 5, grade: '重度污染', color: '#8f3f97', description: '对所有人群很不健康' },
  { level: 6, grade: '严重污染', color: '#7e0023', description: '对所有人群极其不健康' },
]

/**
 * 按等级获取 AQI 展示信息
 * @param {number} level AQI 等级 1-6
 */
export function getAqiInfo(level) {
  return (
    AQI_LEVELS.find((item) => item.level === Number(level)) || {
      level,
      grade: '未知',
      color: '#999999',
      description: '',
    }
  )
}
