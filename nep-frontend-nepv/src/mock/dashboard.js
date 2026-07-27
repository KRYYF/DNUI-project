const PROVINCE_NAMES = [
  '北京市', '天津市', '上海市', '重庆市',
  '河北省', '山西省', '辽宁省', '吉林省', '黑龙江省',
  '江苏省', '浙江省', '安徽省', '福建省', '江西省', '山东省',
  '河南省', '湖北省', '湖南省', '广东省', '海南省',
  '四川省', '贵州省', '云南省', '陕西省', '甘肃省',
  '青海省', '台湾省',
  '内蒙古自治区', '广西壮族自治区', '西藏自治区',
  '宁夏回族自治区', '新疆维吾尔自治区',
  '香港特别行政区', '澳门特别行政区'
]

const AQI_LEVELS = ['优', '良', '轻度污染', '中度污染', '重度污染', '严重污染']

function rand(min, max) {
  return Math.floor(Math.random() * (max - min + 1)) + min
}

export const mockData = {
  provinceExceed: PROVINCE_NAMES.map(name => ({
    provinceName: name,
    so2Exceed: rand(0, 50),
    coExceed: rand(0, 40),
    pm25Exceed: rand(0, 60),
    aqiExceed: rand(0, 80)
  })),

  aqiDistribution: AQI_LEVELS.map((name, i) => ({
    name,
    value: rand(50, 500)
  })),

  aqiTrend: {
    months: ['2026-01', '2026-02', '2026-03', '2026-04', '2026-05', '2026-06',
             '2026-07', '2026-08', '2026-09', '2026-10', '2026-11', '2026-12'],
    exceedCounts: Array.from({ length: 12 }, () => rand(20, 200))
  },

  // 网格覆盖率（后端 /api/statistics/gridCoverage 真实数据）
  // 大城市覆盖率 = 在职网格员覆盖的大城市数(17) ÷ 105(2020人口普查分县资料)
  // 省份覆盖率 = 在职网格员覆盖的省份数(16) ÷ 34(省级行政区)
  gridCoverage: {
    provinceCoverage: 47.06,
    cityCoverage: 16.19
  },

  realTimeCount: {
    totalCount: rand(300, 500),
    goodCount: rand(200, 400),
    pollutionCount: rand(20, 100)
  }
}

export const provinceExceedByCity = () => {
  const data = {}
  PROVINCE_NAMES.forEach(name => {
    data[name] = rand(0, 80)
  })
  return data
}
