<!-- 工单列表 -->
<template>
  <div class='work-order'>
    <div>
      <div class="table-header">
        <div>
          <el-form-item>
            <div width="1000">
              <el-input v-model="search" placeholder="请输入任务名"/>
            </div>
          </el-form-item>
        </div>
      </div>
      <el-table :data="filterTableData" style="width: 100%; height: 480px;" fit :border="true">
        <el-table-column prop="name" label="外勤任务标题"/>
        <el-table-column prop="status" label="任务状态" sortable column-key="status" :filters="[
                { text: '未完成', value: '未完成' },
                { text: '已完成', value: '已完成' },
              ]" :filter-method="filterHandler"/>
        <el-table-column fixed="right" label="操作" width="200">
          <template #default="{ row }">
            <el-button link type="primary" size="small" plain @click="taskDetail(row)">
              详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="page">
        <el-pagination v-model:current-page="currentPage" v-model:page-size="pageSize" :small="small"
                       :disabled="disabled" :background="background" layout="prev, pager, next, jumper" :total="1000"
                       @size-change="handleSizeChange" @current-change="handleCurrentChange"/>
      </div>
    </div>

    <!-- 工单详情弹窗 -->
    <div>
      <el-dialog v-model="detailVisible" title="工单详情" width="600" :before-close="handleClose" center>
        <div>
          <div class="table-content">
            <el-table :data="gridData" :border="true" :fit="true">
              <el-table-column property="name" label="外勤人员姓名"/>
              <el-table-column prop="status" label="状态" sortable column-key="isaccept" :filters="[
                { text: '已接受', value: '已接受' },
                { text: '已拒绝', value: '已拒绝' },
                { text: '待处理', value: '待处理' },
              ]" :filter-method="filterHandler"/>
              <el-table-column width="75" label="操作">
                <template #default="{ row }">
                  <el-button size="small" link type="danger" @click="deleteDialog(row)">
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
      </el-dialog>
    </div>


    <!-- 删除工单 -->
    <div>
      <el-dialog v-model="deleteVisible" title="确认删除" width="400" center>
        <div style="padding-left: 20px;">
          确认删除所选工单吗？
        </div>
        <template #footer>
          <div class="dialog-footer btn-box">
            <el-button type="primary" @click="taskAllocationDelete">
              确认
            </el-button>
            <el-button type="primary" @click="deleteVisible = false">取消</el-button>
          </div>
        </template>
      </el-dialog>
    </div>

  </div>


</template>

<script setup lang='ts'>
import {ref, reactive, onMounted} from 'vue'
import {ElMessage, UploadInstance} from 'element-plus'
import {useRouter} from "vue-router";
import {computed} from "vue";

// 工单列表详细界面
const router = useRouter();
const detail = () => {
  router.push("workOrderDetail");
}

// 编辑弹出框
// const isacceptVisible = ref(false)
// const innerVisible = ref(false)
const deleteVisible = ref(false)
const detailVisible = ref(false)


// 搜索框
const search = ref('')
const filterTableData = computed(() =>
    tableData.value.filter(
        (data) =>
            !search.value ||
            data.name.toLowerCase().includes(search.value.toLowerCase())
    )
)

// 表筛选
const filterHandler = (
    value,
    row,
    column,
) => {
  const property = column['property']
  return row[property] === value
}

const form = reactive({
  identity: '',
  name: '',
  gender: '',
  department: '',
  meil: '',
  phoneNumber: '',
  status: '',
})

let deleteAllocation = ref()
let task = ref()


const tableData = ref([])

const gridData = ref([])

console.log(tableData);

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

function deleteDialog(row) {
  deleteAllocation.value = row
  deleteVisible.value = true
}

function taskAllocationDelete() {
  fetchDeleteTaskAllocation()
}

function taskDetail(row) {
  fetchGetUsersByFieldTaskId(row)
  task.value = row
  detailVisible.value = true
}

const fetchDeleteTaskAllocation = async () => {
  try {
    const token = localStorage.getItem('token');
    const response = await fetch('http://localhost:8080/taskAllocation/deleteAllocationInfo/' + deleteAllocation.value.id + '/' + task.value.id, {
      method: 'DELETE',
      headers: {
        'Authorization': `${token}`,
        'Content-Type': 'application/json'
      },
    });
    const result = await response.json();

    if (result.code === 200) {
      await fetchGetUsersByFieldTaskId(task.value)
      deleteVisible.value = false
    } else {
      ElMessage.error(`Failed to fetch DeleteTaskAllocation: ${result.message}`);
    }
  } catch (error) {
    ElMessage.error('Error fetching DeleteTaskAllocation');
    console.error('Error fetching DeleteTaskAllocation:', error);
  }
}

const fetchGetTaskList = async () => {
  try {
    const token = localStorage.getItem('token');
    const response = await fetch('http://localhost:8080/fieldTask/getFieldTasks', {
      method: 'GET',
      headers: {
        'Authorization': `${token}`,
        'Content-Type': 'application/json'
      },
    });
    const result = await response.json();

    if (result.code === 200) {
      for (let i = 0; i < result.data.length; i++) {
        result.data[i].status = statusChange(result.data[i].status)
        tableData.value.push(result.data[i])
      }
      console.log("@@", result.data[0])
    } else {
      ElMessage.error(`Failed to fetch employees: ${result.message}`);
    }
  } catch (error) {
    ElMessage.error('Error fetching employees');
    console.error('Error fetching employees:', error);
  }
};

function statusChange(status) {
  switch (status) {
    case 0:
      return '待完善';
    case 1:
      return '进行中';
    case 2:
      return '已完成';
  }
}

const fetchGetUsersByFieldTaskId = async (row) => {
  try {
    const token = localStorage.getItem('token');
    const response = await fetch('http://localhost:8080/user/getUsersByFiledTaskId/' + row.id, {
      method: 'GET',
      headers: {
        'Authorization': `${token}`,
        'Content-Type': 'application/json'
      }
    });
    const result = await response.json();

    if (result.code === 200) {
      gridData.value = []
      console.log(result.data)
      if (result.data['0'] !== null) {
        for (let i = 0; i < result.data['0'].length; i++) {
          let user = result.data['0'][i];
          user['status'] = '待处理'
          gridData.value.push(user)
        }
      }
      if (result.data['1'] !== null) {
        for (let i = 0; i < result.data['1'].length; i++) {
          let user = result.data['1'][i];
          user['status'] = '待处理'
          gridData.value.push(user)
        }
      }
      if (result.data['2'] !== null) {
        for (let i = 0; i < result.data['2'].length; i++) {
          let user = result.data['2'][i];
          user['status'] = '待处理'
          gridData.value.push(user)
        }
      }
      // for ()
      console.log(gridData.value)
    } else {
      ElMessage.error(`Failed to fetch employees: ${result.message}`);
    }
  } catch (error) {
    ElMessage.error('Error fetching taskDetail');
    console.error('Error fetching taskDetail:', error);
  }
}

onMounted(() => {
  fetchGetTaskList()
})
</script>

<style lang='scss' scoped>
.page {
  margin-top: 10px;
}

.table-header {
  display: flex;
  justify-content: space-between;
}

.table-content {
  margin-top: 20px;
}

.accept-box {
  .accept {
    padding: 20px;

  }
}

.dialog-footer {
  margin-right: 20px;
  margin-bottom: 20px;
}

.upload,
.edit {
  padding: 20px;
}

.btn-box {
  display: flex;
  justify-content: space-around;

  button {
    width: 100px;
  }
}

.uploadbtn {
  margin-left: 40px;
}
</style>
