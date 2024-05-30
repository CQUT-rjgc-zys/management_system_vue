<!-- 外勤任务列表（接受的外勤任务） -->
<template>
  <div class='my-tasklist'>
    <div>
      <el-table
          :data="filterTableData"
          style="width: 100%;height: 500px;"
          :border="true"
          :fit="true"
      >
        <el-table-column type="selection" width="50"/>
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
        <el-table-column
            prop="status"
            label="任务状态"
            width="120"
            sortable
            column-key="status"
            :filters="[
                { text: '待完善', value: '待完善' },
                { text: '进行中', value: '进行中' },
                { text: '已完成', value: '已完成' },
              ]"
            :filter-method="filterHandler"
        />
        <el-table-column prop="taskSpot" label="任务地点" width="200"/>

        <el-table-column fixed="right" label="Operations" width="110">
          <template #default="{ row }">
            <el-button link type="primary" size="small" plain @click="detail(row)">
              详情
            </el-button>
            <el-button link type="primary" size="small" plain @click="report">打卡</el-button>
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
  </div>
</template>

<script setup lang='ts'>
import {ref, reactive, onMounted} from 'vue'
import {ElMessage, ElTable} from 'element-plus';
import {useRouter} from "vue-router";
import {computed} from "vue";
import axios from "axios";
// 打卡界面
const router = useRouter();
const report = () => {
  router.push("report");
}

const detail = (row) => {
  router.push({path: "/myTaskDetail", query: {id: row.id}});
}

// 搜索框
const search = ref('')
const filterTableData = computed(() =>
    tableData.value.filter(
        (data) =>
            !search.value ||
            data.name.toLowerCase().includes(search.value.toLowerCase())
    )
)


// const form = reactive({
//   name: '',
//   startTime: '',
//   endTime: '',
//   startTime: '',
//   endTime: '',
//   taskBrief: '',
//   description: '',
//   taskSpot: '',
//   status: '',
// })

const tableData = ref([])

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

const user = ref(JSON.parse(localStorage.getItem("user")))

const fetchGetTaskAllocationList = async () => {
  try {
    const token = localStorage.getItem('token');
    const response = await fetch('http://localhost:8080/taskAllocation/getAllTaskAllocationInfoByUserId/' + user.value.id, {
      method: 'GET',
      headers: {
        'Authorization': `${token}`,
        'Content-Type': 'application/json'
      },
    });
    const result = await response.json();

    if (result.code === 200) {
      tableData.value = []
      for (let i = 0; i < result.data.length; i++) {
        await axios.get('http://localhost:8080/fieldTask/getFieldTaskById/' + result.data[i].taskId, {
          headers: {
            'Authorization': `${token}`,
            'Content-Type': 'application/json'
          }
        }).then(res => {
          let task = res.data.data;
          task.status = statusChange(task.status)
          tableData.value.push(task)
        }, err => {
          console.log(err)
        })
      }
    } else {
      ElMessage.error(`Failed to fetch employees: ${result.message}`);
    }
  } catch (error) {
    ElMessage.error('Error fetching employees');
    console.error('Error fetching employees:', error);
  }
}

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
  fetchGetTaskAllocationList()
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

.table-header {
  display: flex;
  justify-content: space-between;
}

.form-content {
  padding: 20px;
}
</style>
