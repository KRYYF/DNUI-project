import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '../store/user'


const routes = [

  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: {
      guest: true,
    },
  },


  {
    path: '/',
    component: () => import('../views/Layout.vue'),

    redirect: '/grid/task',


    children: [


      {
        path: 'index',

        name: 'Index',

        component: () => import('../views/Index.vue'),

        meta: {
          requiresAuth: true,
          title: '首页',
        },

      },


      {
        path: 'grid/task',

        name: 'GridTaskList',

        component: () =>
          import('../views/gridMember/GridTaskList.vue'),

        meta: {
          requiresAuth: true,
          title: '我的任务',
        },

      },


      {
        path: 'grid/detail/:afId',

        name: 'GridTaskDetail',

        component: () =>
          import('../views/gridMember/GridTaskDetail.vue'),

        meta: {
          requiresAuth: true,
          title: '任务详情',
        },

      },


      {
        path: 'grid/submit/:afId',

        name: 'GridSubmit',

        component: () =>
          import('../views/gridMember/GridSubmit.vue'),

        meta: {
          requiresAuth: true,
          title: '提交AQI数据',
        },

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


    next('/grid/task')


  } else {


    next()


  }


})


export default router