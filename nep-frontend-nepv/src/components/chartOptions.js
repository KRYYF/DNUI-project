import * as echarts from 'echarts'

// ==================== 中国地图注册 ====================
let mapRegistered = false
export async function ensureMapRegistered() {
  if (mapRegistered) return true
  try {
    const resp = await fetch('/map/china.json')
    const geoJSON = await resp.json()
    echarts.registerMap('china', geoJSON)
    mapRegistered = true
    return true
  } catch (e) {
    console.error('中国地图 GeoJSON 加载失败', e)
    return false
  }
}

// ==================== 暗色主题常量 ====================
const DARK_THEME = {
  title: {
    textStyle: { color: '#e0e6ed', fontSize: 14, fontWeight: 600 },
    subtextStyle: { color: '#8ea4c7', fontSize: 12 }
  },
  tooltip: {
    backgroundColor: 'rgba(20, 30, 60, 0.9)',
    borderColor: 'rgba(30, 136, 229, 0.5)',
    textStyle: { color: '#e0e6ed', fontSize: 12 }
  },
  legend: {
    textStyle: { color: '#8ea4c7', fontSize: 11 },
    itemWidth: 12,
    itemHeight: 8
  },
  grid: { top: 40, right: 20, bottom: 30, left: 50, containLabel: true }
}

const COLORS = ['#00e5ff', '#ffb300', '#ff5252', '#00e676', '#ab47bc', '#ff9800', '#26c6da', '#ec407a']

// 地图配色：蓝 → 黄 → 橙 → 红（低→高超标）
const MAP_COLORS = ['#0a2a4a', '#1a5276', '#2980b9', '#f39c12', '#e74c3c', '#8e0e0e']

// ==================== 可选标题（空字符串时不渲染） ====================
function makeTitle(text) {
  if (!text) return {}
  return {
    title: {
      text,
      left: 'center',
      top: 5,
      textStyle: { color: '#e0e6ed', fontSize: 14, fontWeight: 600 }
    }
  }
}

// ==================== 空数据占位图形 ====================
function emptyGraphic(text = '暂无数据') {
  return {
    type: 'group',
    left: 'center',
    top: 'center',
    children: [
      {
        type: 'text',
        style: { text, fill: '#8ea4c7', fontSize: 14, textAlign: 'center' },
        left: 'center',
        top: 'center'
      }
    ]
  }
}

/** 判断一组数据是否为空（全零或无元素） */
function isEmptyData(data) {
  if (!data || data.length === 0) return true
  if (Array.isArray(data) && data.every(v => v === 0 || v === '' || v == null)) return true
  return false
}

export function createBarChart(title, data, categories, options = {}) {
  const color = options.color || '#00e5ff'
  const empty = isEmptyData(data)
  return {
    ...makeTitle(title),
    tooltip: { trigger: 'axis', backgroundColor: 'rgba(20, 30, 60, 0.9)', borderColor: 'rgba(30, 136, 229, 0.5)', textStyle: { color: '#e0e6ed', fontSize: 12 } },
    grid: { top: 30, right: 12, bottom: 30, left: 50, containLabel: true },
    graphic: empty ? [emptyGraphic()] : [],
    xAxis: {
      type: 'category',
      data: empty ? [] : categories,
      axisLine: { lineStyle: { color: 'rgba(160, 180, 210, 0.4)' } },
      axisLabel: { color: '#c0ccda', fontSize: 11, rotate: options.rotate || 0 },
      axisTick: { show: false }
    },
    yAxis: {
      type: 'value',
      axisLine: { show: false },
      axisTick: { show: false },
      axisLabel: { color: '#a8b8cc', fontSize: 10 },
      splitLine: { lineStyle: { color: 'rgba(160, 180, 210, 0.15)' } }
    },
    series: empty ? [] : [{
      type: 'bar',
      data: data,
      barWidth: '65%',
      barCategoryGap: '8%',
      itemStyle: {
        color: {
          type: 'linear', x: 0, y: 0, x2: 0, y2: 1,
          colorStops: [
            { offset: 0, color: color },
            { offset: 1, color: color + '40' }
          ]
        },
        borderRadius: [4, 4, 0, 0]
      }
    }]
  }
}

export function createPieChart(title, data) {
  const empty = isEmptyData(data)
  return {
    ...makeTitle(title),
    tooltip: { trigger: 'item', backgroundColor: 'rgba(20, 30, 60, 0.9)', borderColor: 'rgba(30, 136, 229, 0.5)', textStyle: { color: '#e0e6ed', fontSize: 12 } },
    legend: {
      orient: 'vertical',
      left: 0,
      top: 'center',
      itemWidth: 20,
      itemHeight: 10,
      itemGap: 6,
      textStyle: { color: '#d0d8e6', fontSize: 10, padding: [0, 0, 0, 2] }
    },
    graphic: empty ? [emptyGraphic()] : [],
    series: empty ? [] : [{
      type: 'pie',
      radius: ['42%', '64%'],
      center: ['64%', '50%'],
      itemStyle: { borderRadius: 4, borderColor: '#0d1a33', borderWidth: 2 },
      label: { show: true, color: '#d0d8e6', fontSize: 9, formatter: '{b} {c}', position: 'outside' },
      labelLine: { show: true, length: 8, length2: 4, lineStyle: { color: '#8ea4c7', width: 1 } },
      data: data.map((item, i) => ({
        name: item.name.replace(/污染/g, ''),
        value: item.value,
        itemStyle: { color: COLORS[i % COLORS.length] }
      }))
    }]
  }
}

export function createLineChart(title, categories, seriesData, seriesName = '', seriesColor = '#00e5ff') {
  const empty = isEmptyData(seriesData)
  return {
    ...makeTitle(title),
    tooltip: { trigger: 'axis', backgroundColor: 'rgba(20, 30, 60, 0.9)', borderColor: 'rgba(30, 136, 229, 0.5)', textStyle: { color: '#e0e6ed', fontSize: 12 } },
    grid: { top: 30, right: 10, bottom: 24, left: 48, containLabel: true },
    graphic: empty ? [emptyGraphic()] : [],
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: empty ? [] : categories,
      axisLine: { lineStyle: { color: 'rgba(160, 180, 210, 0.4)' } },
      axisLabel: { color: '#a8b8cc', fontSize: 10 },
      axisTick: { show: false }
    },
    yAxis: {
      type: 'value',
      axisLine: { show: false },
      axisTick: { show: false },
      axisLabel: { color: '#a8b8cc', fontSize: 10 },
      splitLine: { lineStyle: { color: 'rgba(160, 180, 210, 0.15)' } }
    },
    series: empty ? [] : [{
      name: seriesName,
      type: 'line',
      smooth: true,
      symbol: 'circle',
      symbolSize: 6,
      data: seriesData,
      lineStyle: { width: 2, color: seriesColor },
      itemStyle: { color: seriesColor },
      areaStyle: {
        color: {
          type: 'linear', x: 0, y: 0, x2: 0, y2: 1,
          colorStops: [
            { offset: 0, color: seriesColor + '60' },
            { offset: 1, color: seriesColor + '05' }
          ]
        }
      }
    }]
  }
}

export function createGaugeChart(title, value, max = 100, color = '#00e5ff') {
  // 仪表盘始终显示，value=0 也是合法值
  value = Number.isFinite(value) ? value : 0
  const displayValue = Number.isFinite(value) ? Number(value.toFixed(2)) : 0
  const steps = 10 // 每 10 一格
  return {
    ...makeTitle(title),
    series: [{
      type: 'gauge',
      center: ['50%', '58%'],
      radius: '80%',
      startAngle: 225,
      endAngle: -45,
      min: 0,
      max: max,
      splitNumber: steps,
      progress: { show: true, width: 10, itemStyle: { color } },
      axisLine: {
        lineStyle: {
          width: 10,
          color: [[1, 'rgba(160, 180, 210, 0.18)']]
        }
      },
      pointer: { show: true, length: '50%', width: 5, itemStyle: { color } },
      axisTick: {
        show: true,
        distance: -12,
        length: 6,
        splitNumber: steps * 2,
        lineStyle: { color: '#a8b8cc', width: 1 }
      },
      splitLine: {
        show: true,
        distance: -16,
        length: 14,
        lineStyle: { color: 'rgba(160, 180, 210, 0.55)', width: 2 }
      },
      axisLabel: {
        show: true,
        distance: -30,
        color: '#c8d4e4',
        fontSize: 10,
        fontWeight: 500
      },
      itemStyle: { color },
      detail: {
        valueAnimation: true,
        fontSize: 22,
        fontWeight: 'bold',
        color: color,
        formatter: function (v) { return v.toFixed(2) },
        offsetCenter: [0, '45%']
      },
      title: { offsetCenter: [0, '65%'], fontSize: 11, color: '#b0bcc8' },
      data: [{ value: displayValue, name: '' }]
    }]
  }
}

/**
 * 中国地图 —— 超标数据按省着色
 * @param {string} title    地图标题
 * @param {Array}  data     [{ name: '北京市', value: 23 }, ...]
 * @param {number} maxVal   用于颜色映射的最大值（可选，默认自动取 data 最大值）
 */
export function createMapChart(title, data, maxVal) {
  const empty = isEmptyData(data)
  const max = maxVal || (empty ? 1 : Math.max(...data.map(d => d.value), 1))

  return {
    ...makeTitle(title),
    tooltip: {
      trigger: 'item',
      backgroundColor: 'rgba(20, 30, 60, 0.92)',
      borderColor: 'rgba(30, 136, 229, 0.5)',
      textStyle: { color: '#e0e6ed', fontSize: 12 },
      formatter: function (p) {
        if (!p.data) return p.name
        const v = p.data.value
        return `<b>${p.name}</b><br/>AQI 超标累计：<b style="color:#ff5252">${v ?? '—'}</b> 次`
      }
    },
    graphic: empty ? [emptyGraphic('暂无超标数据')] : [],
    visualMap: {
      show: true,
      type: 'piecewise',
      min: 0,
      max: max,
      left: 10,
      top: 'center',
      text: ['高', '低'],
      textStyle: { color: '#c0ccda', fontSize: 11 },
      pieces: [
        { min: 0, max: Math.max(1, Math.ceil(max * 0.2)), color: '#0f2640' },
        { min: Math.ceil(max * 0.2) + 1, max: Math.ceil(max * 0.4), color: '#1a5276' },
        { min: Math.ceil(max * 0.4) + 1, max: Math.ceil(max * 0.6), color: '#2980b9' },
        { min: Math.ceil(max * 0.6) + 1, max: Math.ceil(max * 0.8), color: '#f39c12' },
        { min: Math.ceil(max * 0.8) + 1, max: max, color: '#e74c3c' }
      ],
      orient: 'vertical',
      itemWidth: 14,
      itemHeight: 14,
      itemGap: 6,
      align: 'left'
    },
    series: [{
      type: 'map',
      map: 'china',
      roam: 'scale',     // 滚轮缩放 + 拖拽平移
      zoom: 1.15,
      center: [104.5, 37],
      label: {
        show: true,
        color: '#b0bcc8',
        fontSize: 9,
        formatter: function (p) {
          if (!p.data) return p.name
          return p.name.replace(/省|市|自治区|特别行政区|壮族|回族|维吾尔/g, '')
        }
      },
      emphasis: {
        label: {
          show: true,
          color: '#fff',
          fontSize: 12,
          fontWeight: 'bold'
        },
        itemStyle: {
          areaColor: '#ffb300',
          borderColor: '#fff',
          borderWidth: 2,
          shadowBlur: 16,
          shadowColor: 'rgba(255, 179, 0, 0.6)'
        }
      },
      itemStyle: {
        borderColor: 'rgba(160, 180, 210, 0.45)',
        borderWidth: 1,
        areaColor: '#152240'
      },
      data: data
    }]
  }
}

export { DARK_THEME, COLORS }
