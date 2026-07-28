<template>
  <div>
    <h2>我的任务</h2>

    <el-card>
      <el-table
        :data="tasks"
        border
        style="width: 100%"
      >

        <el-table-column
          prop="afId"
          label="任务编号"
          width="120"
        />

        <el-table-column
          prop="address"
          label="地址"
        />

        <el-table-column
          prop="information"
          label="反馈内容"
        />

        <el-table-column
          prop="estimatedGrade"
          label="预估等级"
          width="120"
        />

        <el-table-column
          prop="state"
          label="状态"
          width="100"
        >
          <template #default="scope">

            <el-tag v-if="scope.row.state === 0">
              未指派
            </el-tag>

            <el-tag
              v-else-if="scope.row.state === 1"
              type="warning"
            >
              已指派
            </el-tag>

            <el-tag
              v-else-if="scope.row.state === 2"
              type="success"
            >
              已确认
            </el-tag>

          </template>
        </el-table-column>


        <el-table-column
          label="操作"
          width="120"
        >

          <template #default="scope">

            <el-button
              type="primary"
              size="small"
              @click="goDetail(scope.row.afId)"
            >
              查看
            </el-button>

          </template>

        </el-table-column>


      </el-table>
    </el-card>

  </div>
</template>


<script setup>

import { ref,onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getTasks } from '../../api/gridMember'


const router = useRouter()


const tasks = ref([])



async function loadTasks(){

  const res = await getTasks()

  console.log('任务数据:',res)


  /*
    后端返回：

    {
      code:200,
      data:{
        records:[]
      }
    }

  */

  tasks.value = res.data.records || []

}



function goDetail(id){

  router.push(
    `/grid/detail/${id}`
  )

}



onMounted(()=>{

  loadTasks()

})


</script>