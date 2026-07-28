<template>

  <div>

    <h2>提交AQI数据</h2>


    <el-card>

      <el-form
        :model="form"
        label-width="120px"
      >


        <el-form-item label="任务编号">

          <el-input
            v-model="form.afId"
            disabled
          />

        </el-form-item>



        <el-form-item label="SO₂值">

          <el-input-number
            v-model="form.so2Value"
            :min="0"
          />

        </el-form-item>



        <el-form-item label="CO值">

          <el-input-number
            v-model="form.coValue"
            :min="0"
          />

        </el-form-item>



        <el-form-item label="颗粒物值">

          <el-input-number
            v-model="form.spmValue"
            :min="0"
          />

        </el-form-item>



        <el-form-item>

          <el-button
            type="primary"
            @click="submit"
          >
            提交
          </el-button>


        </el-form-item>



      </el-form>


    </el-card>


  </div>


</template>



<script setup>


import {
  reactive,
  onMounted
} from 'vue'


import {
  useRoute,
  useRouter
} from 'vue-router'


import {
  submitAqi
} from '../../api/gridMember'



const route = useRoute()

const router = useRouter()



const form = reactive({

  afId: null,

  so2Value: 0,

  coValue: 0,

  spmValue: 0

})





onMounted(()=>{


  form.afId = Number(
    route.params.afId
  )


})





async function submit(){


  const res = await submitAqi(form)


  console.log(
    '提交结果:',
    res
  )


  ElMessage.success(
    '提交成功'
  )


  router.push(
    '/grid/task'
  )


}



</script>