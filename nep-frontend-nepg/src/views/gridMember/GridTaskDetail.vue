<template>

  <div>

    <h2>任务详情</h2>


    <el-card v-if="task">

      <el-descriptions
        title="反馈信息"
        :column="1"
        border
      >

        <el-descriptions-item label="任务编号">
          {{ task.afId }}
        </el-descriptions-item>


        <el-descriptions-item label="联系电话">
          {{ task.telId }}
        </el-descriptions-item>


        <el-descriptions-item label="地址">
          {{ task.address }}
        </el-descriptions-item>


        <el-descriptions-item label="反馈内容">
          {{ task.information }}
        </el-descriptions-item>


        <el-descriptions-item label="预估等级">
          {{ task.estimatedGrade }}
        </el-descriptions-item>


        <el-descriptions-item label="状态">

          <el-tag v-if="task.state===0">
            未指派
          </el-tag>


          <el-tag
            v-else-if="task.state===1"
            type="warning"
          >
            已指派
          </el-tag>


          <el-tag
            v-else
            type="success"
          >
            已确认
          </el-tag>


        </el-descriptions-item>


      </el-descriptions>


      <div style="margin-top:20px">

        <el-button
          type="primary"
          @click="goSubmit"
        >
          提交AQI数据
        </el-button>


      </div>


    </el-card>


  </div>


</template>



<script setup>


import {
  ref,
  onMounted
} from 'vue'


import {
  useRoute,
  useRouter
} from 'vue-router'


import {
  getTaskDetail
} from '../../api/gridMember'



const route = useRoute()

const router = useRouter()


const task = ref(null)



async function loadDetail(){


  const afId = route.params.afId


  const res = await getTaskDetail(afId)


  console.log('详情数据:',res)


  task.value = res.data


}



function goSubmit(){


  router.push(
    `/grid/submit/${task.value.afId}`
  )


}



onMounted(()=>{

  loadDetail()

})



</script>