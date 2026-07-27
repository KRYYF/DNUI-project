import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '../store/user'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: { guest: true },
  },
  {
    path: '/',
    component: () => import('../views/Layout.vue'),
    redirect: '/index',
    children: [
      {
        path: 'index',
        name: 'Index',
        component: () => import('../views/Index.vue'),
        meta: { requiresAuth: true, title: '首页' },
      },
      {
        path: 'feedback/list',
        name: 'FeedbackList',
        component: () => import('../views/feedback/FeedbackList.vue'),
        meta: { requiresAuth: true, title: '公众监督数据列表' },
      },
      {
        path: 'feedback/detail/:id',
        name: 'FeedbackDetail',
        component: () => import('../views/feedback/FeedbackDetail.vue'),
        meta: { requiresAuth: true, title: '公众监督数据详情' },
      },
      {
        path: 'feedback/assign/:id',
        name: 'AssignFeedback',
        component: () => import('../views/feedback/AssignFeedback.vue'),
        meta: { requiresAuth: true, title: '指派网格员' },
      },
      {
        path: 'confirmed/list',
        name: 'ConfirmedList',
        component: () => import('../views/confirmed/ConfirmedList.vue'),
        meta: { requiresAuth: true, title: '确认AQI数据列表' },
      },
      {
        path: 'confirmed/detail/:id',
        name: 'ConfirmedDetail',
        component: () => import('../views/confirmed/ConfirmedDetail.vue'),
        meta: { requiresAuth: true, title: '确认AQI数据详情' },
      },
      {
        path: 'statistics/province',
        name: 'ProvinceExceed',
        component: () => import('../views/statistics/ProvinceExceed.vue'),
        meta: { requiresAuth: true, title: '省分组检查统计' },
      },
      {
        path: 'statistics/distribution',
        name: 'AqiDistribution',
        component: () => import('../views/statistics/AqiDistribution.vue'),
        meta: { requiresAuth: true, title: 'AQI指数分布统计' },
      },
      {
        path: 'statistics/trend',
        name: 'AqiTrend',
        component: () => import('../views/statistics/AqiTrend.vue'),
        meta: { requiresAuth: true, title: 'AQI指数趋势统计' },
      },
      {
        path: 'statistics/other',
        name: 'OtherStats',
        component: () => import('../views/statistics/OtherStats.vue'),
        meta: { requiresAuth: true, title: '其它数据统计' },
      },
    ],
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  const isLoggedIn = !!userStore.token

  if (to.meta.requiresAuth && !isLoggedIn) {
    next('/login')
  } else if (to.meta.guest && isLoggedIn) {
    next('/index')
  } else {
    next()
  }
})

export default router
