<!-- 外勤信息列表 -->
<template>
  <div class='tasklist'>
    <router-view></router-view>
    <div>
      <div class="table-header">
        <div>
          <el-button type="primary" plain @click="addFormVisible = true">
            新增外勤任务
          </el-button>
          <el-button type="danger" plain @click="deleteFormVisible = true">
            删除外勤任务
          </el-button>
        </div>
        <div>
          <el-form-item>
            <div width="1000">
              <el-input v-model="search" placeholder="请输入任务名"/>
            </div>
          </el-form-item>
        </div>
      </div>
      <el-table :data="filterTableData" style="width: 100%;height: 480px;" :border="true" fit
                @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="40"/>
        <el-table-column prop="name" label="外勤任务名"/>
        <el-table-column prop="startTime" label="开始时间">
          <template #default="scope">
            {{ formatDateTime(scope.row.startTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="endTime" label="截止时间">
          <template #default="scope">
            {{ formatDateTime(scope.row.endTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="taskBrief" label="外勤任务简介" width="150"/>
        <el-table-column prop="taskSpot" label="任务地点" width="200"/>
        <!-- <el-table-column prop="status" label="任务状态"  /> -->
        <el-table-column prop="status" label="任务状态" width="120" sortable column-key="status" :filters="[
          { text: '未完成', value: '未完成' },
          { text: '已完成', value: '已完成' },
        ]" :filter-method="filterHandler"/>
        <el-table-column fixed="right" label="Operations">
          <template #default="scope">
            <RouterLink :to="{path: '/taskDetailInfo', query: {taskId: scope.row.id}}">
              <el-button type="text" size="small">详情</el-button>
            </RouterLink>
          </template>
        </el-table-column>

      </el-table>
      <div class="page">
        <el-pagination v-model:current-page="currentPage" v-model:page-size="pageSize" :small="small"
                       :disabled="disabled" :background="background" layout="prev, pager, next, jumper" :total="1000"
                       @size-change="handleSizeChange" @current-change="handleCurrentChange"/>
      </div>
    </div>


    <!-- 新增外勤任务 -->
    <div>
      <el-dialog v-model="addFormVisible" title="新增外勤任务" width="500" draggable>
        <div>
          <div>
            <el-form :model="form" label-width="auto" class="form-content">
              <el-form-item label="任务名">
                <el-input type="text" v-model="form.name"/>
              </el-form-item>
              <el-form-item label="任务时间">
                <el-col :span="11">
                  <el-date-picker
                      v-model="form.startTime"
                      type="date"
                      placeholder="开始时间"
                      style="width: 100%"
                  />
                </el-col>
                <el-col :span="2" class="text-center">
                  <span class="text-gray-500">-</span>
                </el-col>
                <el-col :span="11">
                  <el-date-picker
                      v-model="form.endTime"
                      type="date"
                      placeholder="截止时间"
                      style="width: 100%"
                  />
                </el-col>
              </el-form-item>
              <el-form-item label="任务简介">
                <el-input v-model="form.taskBrief" type="textarea"/>
              </el-form-item>
            </el-form>
          </div>
          <div class="btn-box">
            <el-button type="primary" @click="taskAdd">新增</el-button>
            <el-button type="primary" @click="addFormVisible = false">取消</el-button>
          </div>
        </div>
      </el-dialog>
    </div>

    <!-- 删除外勤任务 -->
    <div>
      <el-dialog v-model="deleteFormVisible" title="确认删除" width="400" :before-close="handleClose" center>
        <div style="padding-left: 20px;">
          确认删除所选外勤任务吗？
        </div>
        <template #footer>
          <div class="dialog-footer btn-box">
            <el-button type="primary" @click="deleteFormVisible = false">
              确认
            </el-button>
            <el-button type="primary" @click="deleteFormVisible = false">取消</el-button>
          </div>
        </template>
      </el-dialog>
    </div>

    <!-- 修改外勤任务信息 -->
    <div>
      <el-dialog v-model="editFormVisible" title="修改外勤任务" width="400" draggable>
        <div>
          <div>
            <el-form :model="form" label-width="auto" style="max-width: 1000px" class="form-content">
              <el-form-item label="任务名">
                <el-input v-model="form.name"/>
              </el-form-item>
              <el-form-item label="开始时间">
                <el-input v-model="form.startTime"/>
              </el-form-item>
              <el-form-item label="截止时间">
                <el-input v-model="form.endTime"/>
              </el-form-item>
              <el-form-item label="外勤任务信息简介">
                <el-input v-model="form.taskBrief" type="textarea"/>
              </el-form-item>
              <el-form-item label="外勤任务详细信息">
                <el-input v-model="form.description" type="textarea"/>
              </el-form-item>
              <el-form-item label="任务地点">
                <el-input v-model="form.taskSpot"/>
              </el-form-item>

            </el-form>
          </div>
          <div>
            <el-button type="primary" @click="editFormVisible = false">确认</el-button>
            <el-button type="primary" @click="editFormVisible = false">取消</el-button>
          </div>
        </div>
      </el-dialog>
    </div>

  </div>

</template>

<script setup lang='ts'>
import {ref, reactive, computed, onMounted} from 'vue'
import {useRouter} from "vue-router";
import {ElMessage} from "element-plus";


// 详情界面
const router = useRouter();
const detail = (row) => {
  router.push({path: "/taskDetailInfo", params: {taskId: row.id}});
}

// 编辑弹出框
let addFormVisible = ref(false)
const deleteFormVisible = ref(false)
const editFormVisible = ref(false);

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


const form = reactive({
  name: '',
  startTime: '',
  endTime: '',
  taskBrief: '',
  description: '',
  taskSpot: null,
  status: '',
})

const tableData = ref([])

function taskAdd() {
  fetchAddTask()
  addFormVisible.value = false
}

const fetchAddTask = async () => {
  try {
    const token = localStorage.getItem('token');
    const response = await fetch('http://localhost:8080/fieldTask/addFieldTask', {
      method: 'POST',
      headers: {
        'Authorization': `${token}`,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(form)
    });
    const result = await response.json();

    if (result.code === 200) {
      ElMessage.success('新增任务成功')
    } else {
      ElMessage.error(`Failed to fetch employees: ${result.message}`);
    }
  } catch (error) {
    ElMessage.error('Error fetching employees');
    console.error('Error fetching employees:', error);
  }
};

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

function formatDateTime(date) {
  let time = new Date(date)
  const switchFormat = time => {
    let y = time.getFullYear(); // 年
    let m = (time.getMonth() + 1).toString().padStart(2, '0'); // 月
    let d = time.getDate().toString().padStart(2, '0'); // 日
    return (
        y + "-" + m + "-" + d
    );
  }
  return switchFormat(time)
}
onMounted(() => {
  fetchGetTaskList()
})
</script>

<style lang='scss' scoped>
.headbtn {
  display: flex;
  justify-content: space-between;
}

.page {
  margin-top: 10px;
}

.btn-box {
  display: flex;
  justify-content: space-around;

  button {
    width: 100px;
  }
}

.table-header {
  display: flex;
  justify-content: space-between;
}

.form-content {
  padding: 10px 20px;
}
</style>
