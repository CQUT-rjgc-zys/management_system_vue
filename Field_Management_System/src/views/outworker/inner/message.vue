<!-- 工单列表 -->
<template>
    <div class='work-order'>
        <div class="work-order-table">
          <el-table :data="tableData" style="width: 80%;height: 500px;" fit>
            <el-table-column prop="id" label="序号" width="80"/>
            <el-table-column prop="dkInfo" label="提示信息"/>
            
            <el-table-column fixed="right"  width="200">
            <template #default>
                <el-badge is-dot class="item">
                  <el-button type="primary" size="small" plain @click="dialogFormVisible = true">
                详情
                </el-button>
                </el-badge>
                <el-badge class="item">
                  <el-button type="primary" size="small" plain @click="dialogFormVisible = true">
                  已读
                  </el-button>
                </el-badge>
                
            </template>
            </el-table-column>
          </el-table>
        <div class="page">
            <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :small="small"
            :disabled="disabled"
            :background="background"
            layout="prev, pager, next, jumper"
            :total="1000"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            />     
        </div>
        </div>

        <!-- 弹窗 -->
        <div>
         <el-dialog v-model="dialogFormVisible" title="提醒信息" width="400" draggable>
                <div>
                    <div>
                      
                    </div>
                </div>
            </el-dialog>
        </div>
        
    </div>
</template>
    
<script setup lang='ts'>
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Action } from 'element-plus'

// 编辑弹出框
const dialogFormVisible = ref(false)
const formLabelWidth = '140px'


const form = reactive({
  dkInfo: '',
  receive: '',
})

const tableData = [
  {
    id:1,
    dkInfo: '你的外勤任务即将到截止日期，请按时完成任务哦'
  },
  {
    id:2,
    dkInfo: '该到地方打卡啦'
  },
  {
    id:3,
    dkInfo: '记得打印文件哦'
  },
  {
    id:4,
    dkInfo: '记得打卡'
  },
 
  ]
  // 分页
const currentPage = ref(1)

const pageSize = ref(100)

const small = ref(false)
const background = ref(false)
const disabled = ref(false)

const handleSizeChange = (val) => {
  console.log(`${val} items per page`)
}
const handleCurrentChange = (val) => {
  console.log(`current page: ${val}`)
}

</script>
    
<style lang='scss' scoped>
.work-order-table{
    padding: 20px;
}
.page{
    margin-top: 10px;
} 

.btn-box{
  display: flex;
  justify-content: space-around;
}

.form-content{
  padding: 20px;
}
.item {
  margin-top: 10px;
  margin-right: 40px;
}
</style>