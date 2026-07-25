<template>
  <el-container class="layout">
    <!-- 左侧菜单 -->
    <el-aside width="220px" class="layout-aside">
      <div class="logo">
        <div class="logo-icon">
          <svg viewBox="0 0 48 48" fill="none" xmlns="http://www.w3.org/2000/svg">
            <rect x="4" y="4" width="40" height="40" rx="10" fill="url(#logoGrad)" />
            <path d="M14 30L22 18L30 24L36 14" stroke="#fff" stroke-width="3" stroke-linecap="round" stroke-linejoin="round"/>
            <circle cx="36" cy="14" r="3" fill="#fff"/>
            <defs>
              <linearGradient id="logoGrad" x1="0" y1="0" x2="48" y2="48">
                <stop offset="0%" stop-color="#43A047"/>
                <stop offset="100%" stop-color="#1B5E20"/>
              </linearGradient>
            </defs>
          </svg>
        </div>
        <span class="logo-text">NEPM 管理端</span>
      </div>
      <el-menu
        :default-active="activeMenu"
        :default-openeds="['feedback-mgmt', 'statistics-mgmt']"
        background-color="#1a2e1a"
        text-color="rgba(255,255,255,0.7)"
        active-text-color="#6ED080"
        router
      >
        <el-sub-menu index="feedback-mgmt">
          <template #title>
            <el-icon><Document /></el-icon>
            <span>公众监督数据管理</span>
          </template>
          <el-menu-item index="/feedback/list">
            <el-icon><List /></el-icon>
            <span>公众监督数据列表</span>
          </el-menu-item>
          <el-menu-item index="/confirmed/list">
            <el-icon><Checked /></el-icon>
            <span>确认AQI数据列表</span>
          </el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="statistics-mgmt">
          <template #title>
            <el-icon><DataAnalysis /></el-icon>
            <span>统计数据管理</span>
          </template>
          <el-menu-item index="/statistics/province">
            <el-icon><DataBoard /></el-icon>
            <span>省分组检查统计</span>
          </el-menu-item>
          <el-menu-item index="/statistics/distribution">
            <el-icon><PieChart /></el-icon>
            <span>AQI指数分布统计</span>
          </el-menu-item>
          <el-menu-item index="/statistics/trend">
            <el-icon><TrendCharts /></el-icon>
            <span>AQI指数趋势统计</span>
          </el-menu-item>
          <el-menu-item index="/statistics/other">
            <el-icon><Monitor /></el-icon>
            <span>其它数据统计</span>
          </el-menu-item>
        </el-sub-menu>
      </el-menu>
    </el-aside>

    <!-- 右侧 -->
    <el-container>
      <el-header class="layout-header">
        <div class="header-left">
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/index' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item v-if="pageTitle">{{ pageTitle }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="header-right">
          <span class="user-info">管理员：{{ userStore.adminCode }}</span>
          <el-button type="danger" text @click="handleLogout">退出登录</el-button>
        </div>
      </el-header>
      <el-main class="layout-main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '../store/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const activeMenu = computed(() => route.path)
const pageTitle = computed(() => route.meta.title || '')

function handleLogout() {
  userStore.clearUser()
  router.push('/login')
}
</script>

<style scoped>
.layout {
  height: 100vh;
}
.layout-aside {
  background: linear-gradient(180deg, #1a2e1a 0%, #1b3a1b 40%, #1a301a 100%);
  overflow-y: auto;
}
.layout-aside :deep(.el-menu) {
  border-right: none;
}
.layout-aside :deep(.el-sub-menu__title) {
  transition: all 0.3s ease;
}
.layout-aside :deep(.el-sub-menu__title:hover) {
  background-color: rgba(67, 160, 71, 0.15) !important;
  color: #fff !important;
}
.layout-aside :deep(.el-menu-item) {
  transition: all 0.3s ease;
}
.layout-aside :deep(.el-menu-item:hover) {
  background-color: rgba(67, 160, 71, 0.2) !important;
  color: #fff !important;
}
.layout-aside :deep(.el-menu-item.is-active) {
  background: linear-gradient(90deg, rgba(67, 160, 71, 0.35), rgba(67, 160, 71, 0.12)) !important;
  color: #6ED080 !important;
  border-right: 3px solid #43A047;
}
.layout-aside :deep(.el-sub-menu.is-active .el-sub-menu__title) {
  color: #6ED080 !important;
}
.logo {
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.08);
  background: rgba(0, 0, 0, 0.15);
}
.logo-icon {
  width: 32px;
  height: 32px;
  flex-shrink: 0;
}
.logo-icon svg {
  width: 100%;
  height: 100%;
  display: block;
}
.logo-text {
  color: #e8f5e9;
  font-size: 17px;
  font-weight: 700;
  letter-spacing: 3px;
}
.layout-header {
  background: #fff;
  border-bottom: 1px solid #e8f0e8;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  height: 60px;
  box-shadow: 0 1px 6px rgba(0, 0, 0, 0.04);
}
.header-left :deep(.el-breadcrumb__inner) {
  color: #555;
  transition: color 0.2s;
}
.header-left :deep(.el-breadcrumb__inner:hover) {
  color: #43A047;
}
.header-right {
  display: flex;
  align-items: center;
  gap: 16px;
}
.user-info {
  color: #555;
  font-size: 14px;
}
.layout-main {
  background: #f4f7f4;
  min-height: calc(100vh - 60px);
  padding: 24px;
}
</style>
