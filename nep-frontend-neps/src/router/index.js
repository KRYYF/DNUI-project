import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '../store/user'

const routes = [
  { path: '/', redirect: '/login' },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: { guest: true },
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue'),
    meta: { guest: true },
  },
  {
    path: '/selectGrid',
    name: 'SelectGrid',
    component: () => import('../views/SelectGrid.vue'),
    meta: { requiresAuth: true },
  },
  {
    path: '/feedback',
    name: 'Feedback',
    component: () => import('../views/Feedback.vue'),
    meta: { requiresAuth: true },
  },
  {
    path: '/history',
    name: 'HistoryList',
    component: () => import('../views/HistoryList.vue'),
    meta: { requiresAuth: true },
  },

  // ========== 新增网格员路由（你新建在 views/grid 目录）==========
  {
    path: '/gridLogin',
    name: 'GridLogin',
    component: () => import('../views/grid/GridLogin.vue')
  },
  {
    path: '/taskList',
    name: 'TaskList',
    component: () => import('../views/grid/TaskList.vue')
  },
  {
    path: '/taskDetail',
    name: 'TaskDetail',
    component: () => import('../views/grid/TaskDetail.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  const loggedIn = Boolean(userStore.token)

  if (to.meta.requiresAuth && !loggedIn) {
    next('/login')
    return
  }
  if (to.meta.guest && loggedIn && (to.path === '/login' || to.path === '/register')) {
    next('/selectGrid')
    return
  }
  next()
})

export default router
