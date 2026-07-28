<template>
  <el-container class="layout">

    <!-- 左侧菜单 -->
    <el-aside width="220px" class="layout-aside">

      <div class="logo">

        <div class="logo-icon">
          <svg 
            viewBox="0 0 48 48" 
            fill="none" 
            xmlns="http://www.w3.org/2000/svg"
          >

            <rect 
              x="4" 
              y="4" 
              width="40" 
              height="40" 
              rx="10" 
              fill="url(#logoGrad)"
            />

            <path 
              d="M14 30L22 18L30 24L36 14"
              stroke="#fff"
              stroke-width="3"
              stroke-linecap="round"
              stroke-linejoin="round"
            />

            <circle 
              cx="36"
              cy="14"
              r="3"
              fill="#fff"
            />

            <defs>

              <linearGradient 
                id="logoGrad"
                x1="0"
                y1="0"
                x2="48"
                y2="48"
              >

                <stop 
                  offset="0%"
                  stop-color="#43A047"
                />

                <stop 
                  offset="100%"
                  stop-color="#1B5E20"
                />

              </linearGradient>

            </defs>

          </svg>
        </div>


        <span class="logo-text">
          NEPG 网格员端
        </span>

      </div>



      <el-menu

        :default-active="activeMenu"

        background-color="#1a2e1a"

        text-color="rgba(255,255,255,0.7)"

        active-text-color="#6ED080"

        router

      >


        <el-menu-item index="/index">

          <el-icon>
            <House />
          </el-icon>

          <span>
            首页
          </span>

        </el-menu-item>



        <el-menu-item index="/grid/task">

          <el-icon>
            <List />
          </el-icon>

          <span>
            我的任务
          </span>

        </el-menu-item>



      </el-menu>


    </el-aside>



    <!-- 右侧 -->

    <el-container>


      <el-header class="layout-header">


        <div class="header-left">

          <el-breadcrumb separator="/">

            <el-breadcrumb-item 
              :to="{path:'/index'}"
            >
              首页
            </el-breadcrumb-item>


            <el-breadcrumb-item 
              v-if="pageTitle"
            >

              {{pageTitle}}

            </el-breadcrumb-item>


          </el-breadcrumb>


        </div>




        <div class="header-right">


          <span class="user-info">

            网格员：
            {{userStore.gmCode}}

          </span>


          <el-button

            type="danger"

            text

            @click="handleLogout"

          >

            退出登录


          </el-button>


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

import { useRoute,useRouter } from 'vue-router'

import { useUserStore } from '../store/user'



const route = useRoute()

const router = useRouter()


const userStore = useUserStore()



const activeMenu = computed(()=>route.path)


const pageTitle = computed(()=>route.meta.title || '')



function handleLogout(){

  userStore.clearUser()

  router.push('/login')

}


</script>





<style scoped>

.layout{

  height:100vh;

}



.layout-aside{

  background:linear-gradient(
    180deg,
    #1a2e1a 0%,
    #1b3a1b 40%,
    #1a301a 100%
  );

}


.layout-aside :deep(.el-menu){

  border-right:none;

}



.logo{

  height:64px;

  display:flex;

  align-items:center;

  justify-content:center;

  gap:10px;

  border-bottom:1px solid rgba(255,255,255,.08);

}



.logo-icon{

  width:32px;

  height:32px;

}


.logo-icon svg{

  width:100%;

  height:100%;

}



.logo-text{

  color:#e8f5e9;

  font-size:17px;

  font-weight:700;

  letter-spacing:2px;

}




.layout-header{

  background:#fff;

  border-bottom:1px solid #e8f0e8;

  display:flex;

  justify-content:space-between;

  align-items:center;

  padding:0 24px;

}



.header-right{

  display:flex;

  align-items:center;

  gap:16px;

}



.user-info{

  color:#555;

}



.layout-main{

  background:#f4f7f4;

  min-height:calc(100vh - 60px);

  padding:24px;

}



</style>