<template>
  <div class="index-page">
    <!-- 欢迎横幅 -->
    <div class="welcome-banner">
      <div class="banner-content">
        <h2 class="page-title">欢迎使用 NEPM 系统管理端</h2>
        <p class="page-subtitle">东软环保公众监督系统 — 管理控制台</p>
      </div>
      <div class="banner-decor">
        <div class="decor-circle c1"></div>
        <div class="decor-circle c2"></div>
        <div class="decor-circle c3"></div>
      </div>
    </div>

    <el-row :gutter="20" class="stat-cards">
      <el-col :span="8" v-for="card in statCards" :key="card.label">
        <el-card shadow="hover" class="stat-card" @click="router.push(card.link)">
          <div class="stat-icon" :style="{ background: card.color }">
            <el-icon :size="28"><component :is="card.icon" /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-label">{{ card.label }}</div>
            <div class="stat-desc">{{ card.desc }}</div>
          </div>
          <div class="stat-arrow">
            <el-icon><ArrowRight /></el-icon>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { Document, Checked, DataBoard, PieChart, TrendCharts, Monitor, ArrowRight } from '@element-plus/icons-vue'

const router = useRouter()

const statCards = [
  {
    label: '公众监督数据',
    desc: '查看和管理公众监督员反馈',
    icon: Document,
    color: 'linear-gradient(135deg, #43A047, #2E7D32)',
    link: '/feedback/list',
  },
  {
    label: '确认AQI数据',
    desc: '查看网格员实测AQI数据',
    icon: Checked,
    color: 'linear-gradient(135deg, #66BB6A, #388E3C)',
    link: '/confirmed/list',
  },
  {
    label: '省分组统计',
    desc: '各省分项超标累计统计',
    icon: DataBoard,
    color: 'linear-gradient(135deg, #FFB74D, #F57C00)',
    link: '/statistics/province',
  },
  {
    label: 'AQI分布统计',
    desc: '空气质量指数级别分布',
    icon: PieChart,
    color: 'linear-gradient(135deg, #E57373, #D32F2F)',
    link: '/statistics/distribution',
  },
  {
    label: 'AQI趋势统计',
    desc: '月度超标趋势变化',
    icon: TrendCharts,
    color: 'linear-gradient(135deg, #4FC3F7, #0288D1)',
    link: '/statistics/trend',
  },
  {
    label: '其它数据统计',
    desc: '检测数量和网格覆盖率',
    icon: Monitor,
    color: 'linear-gradient(135deg, #BA68C8, #7B1FA2)',
    link: '/statistics/other',
  },
]
</script>

<style scoped>
.index-page {
  max-width: 1200px;
}

/* 欢迎横幅 */
.welcome-banner {
  position: relative;
  background: linear-gradient(135deg, #1B5E20 0%, #2E7D32 40%, #43A047 100%);
  border-radius: 16px;
  padding: 32px 40px;
  margin-bottom: 28px;
  overflow: hidden;
  box-shadow:
    0 4px 16px rgba(27, 94, 32, 0.18),
    0 8px 32px rgba(67, 160, 71, 0.12);
}
.banner-content {
  position: relative;
  z-index: 1;
}
.banner-decor {
  position: absolute;
  inset: 0;
  pointer-events: none;
}
.decor-circle {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.06);
}
.decor-circle.c1 {
  width: 200px;
  height: 200px;
  top: -60px;
  right: 30px;
}
.decor-circle.c2 {
  width: 140px;
  height: 140px;
  bottom: -40px;
  right: 180px;
}
.decor-circle.c3 {
  width: 80px;
  height: 80px;
  top: 20px;
  right: 280px;
}

.page-title {
  margin: 0;
  font-size: 24px;
  font-weight: 700;
  color: #fff;
  letter-spacing: 3px;
}
.page-subtitle {
  margin: 6px 0 0;
  font-size: 13px;
  color: rgba(255, 255, 255, 0.8);
  letter-spacing: 1px;
}

/* 统计卡片 */
.stat-cards {
  margin: 0 !important;
}
.stat-card {
  margin-bottom: 20px;
  cursor: pointer;
  border-radius: 16px;
  border: 1px solid #eef3ee;
  transition: all 0.35s cubic-bezier(0.16, 1, 0.3, 1);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}
.stat-card:hover {
  transform: translateY(-6px);
  border-color: #c8e6c9;
  box-shadow: 0 8px 28px rgba(67, 160, 71, 0.15);
}
.stat-card :deep(.el-card__body) {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 28px 24px;
}
.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  flex-shrink: 0;
  box-shadow: 0 4px 14px rgba(0, 0, 0, 0.12);
}
.stat-info {
  flex: 1;
}
.stat-label {
  font-size: 16px;
  font-weight: 700;
  color: #1a1a2e;
}
.stat-desc {
  font-size: 13px;
  color: #909399;
  margin-top: 4px;
}
.stat-arrow {
  color: #c0c4cc;
  font-size: 16px;
  transition: all 0.3s;
}
.stat-card:hover .stat-arrow {
  color: #43A047;
  transform: translateX(4px);
}
</style>
