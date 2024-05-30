<!-- 外勤任务详细信息 -->
<template>
  <div class='issue-task'>
    <el-page-header @back="goBack">
      <template #content>
        <span class="text-large font-600 mr-3"> 工单列表详细界面 </span>
      </template>
    </el-page-header>
    <div>
      <div class="form-content">
        <el-descriptions title=" 和**谈合作" :column="1">
          <el-descriptions-item label="任务状态">
            <el-tag size="small">未完成</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="任务简介">
            No.1188, Wuzhong Avenue, Wuzhong District, Suzhou, Jiangsu Province
          </el-descriptions-item>
        </el-descriptions>
      </div>

      <div class="table-content">
        <el-table :data="gridData" :border="true" :fit="true">
          <el-table-column property="name" label="外勤人员姓名" />
          <el-table-column property="phoneNumber" label="联系方式" />
          <el-table-column prop="isaccept" label="是否接受" sortable column-key="isaccept" :filters="[
            { text: '已接受', value: '已接受' },
            { text: '已拒绝', value: '已拒绝' },
          ]" :filter-method="filterHandler" />
          <el-table-column width="75">
            <template #default="scope">
              <el-button size="small" type="primary" plain @click="deleteVisible = true">
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
    <div style="margin-top: 20px; float: left;">
      <el-button type="primary" @click="detailVisible = true">
        确认并发布任务详细信息
      </el-button>
    </div>
    <!-- 发布详细信息 -->
    <el-dialog v-model="detailVisible" width="500" title="发布外勤任务详细信息" draggable center>
      <div class="upload">
        <div>
          <el-form :model="form" label-width="auto" class="form-content">
            <el-form-item label="外勤任务名">
              <el-input v-model="form.name" value="和**谈合作"></el-input>
            </el-form-item>
            <el-form-item label="外勤任务详细信息">
              <el-input v-model="form.description" type="textarea" value="任务详细信息"></el-input>
            </el-form-item>
          </el-form>
        </div>
        <div>
          <el-upload v-model:file-list="fileList" class="upload-demo" style="padding: 0 20px;"
            action="https://run.mocky.io/v3/9d059bf9-4660-45f2-925d-ce80ad6c4d15" :on-preview="handlePreview"
            :on-remove="handleRemove" :before-remove="beforeRemove" :limit="5" :on-exceed="handleExceed">
            <el-button type="primary" plain size="small">上传文件</el-button>
            <template #tip>
              <div class="el-upload__tip">
                请选择该外勤任务相关文件.
              </div>
            </template>
          </el-upload>
        </div>
      </div>
      <template #footer>
        <div>
          <el-button type="primary" @click="detailVisible = false">
            发布任务详细信息
          </el-button>
        </div>
      </template>
    </el-dialog>


    <!-- 删除拒绝的外勤人员 -->
    <el-dialog v-model="deleteVisible" title="确认删除" width="400" :before-close="handleClose" draggable>
      <div>
        确认删除所选外勤人员吗？
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="deleteVisible = false">取消</el-button>
          <el-button type="primary" @click="deleteVisible = false">
            确认
          </el-button>
        </div>
      </template>
    </el-dialog>

  </div>
</template>

<script setup lang='ts'>
import { ref, reactive } from 'vue'
import { useRouter } from "vue-router"
import { ElMessage, ElMessageBox } from 'element-plus'
import { UploadProps, UploadUserFile } from 'element-plus'



// 返回外勤任务列表
const router = useRouter();
const goBack = () => {
  router.push("workOrderList");
}

// 弹出框
const issueVisible = ref(false)
const deleteVisible = ref(false)
const detailVisible = ref(false)

// 表筛选
const filterHandler = (
  value,
  row,
  column,
) => {
  const property = column['property']
  return row[property] === value
}


// 分页
const currentPage = ref(1)

const pageSize = ref(100)

const small = ref(false)
const background = ref(false)
const disabled = ref(false)

const handleSizeChange = (val) => {
  console.log(`${val} 行每页`)
}
const handleCurrentChange = (val) => {
  console.log(`当前页码: ${val}`)
}


const form = reactive({
  name: '',
  gender: '',
  jobNumber: '',
  department: '',
})


const tableData = [
  {
    name: '叶舒华',
    gender: '女',
    jobNumber: '12023020201',
    department: '销售部',
  },
  {
    name: '宋雨琦',
    gender: '女',
    jobNumber: '12023020201',
    department: '销售部',
  },
  {
    name: '田小娟',
    gender: '女',
    jobNumber: '12023020201',
    department: '销售部',
  },
  {
    name: '金米妮',
    gender: '女',
    jobNumber: '12023020201',
    department: '销售部',
  },
  {
    name: '赵美延',
    gender: '女',
    jobNumber: '12023020201',
    department: '销售部',
  },
]


</script>

<style lang='scss' scoped>
.page {
  margin-top: 10px;
}

.table-header {
  float: right;
}

.form-content {
  padding: 20px;
}

</style>