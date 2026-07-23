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
