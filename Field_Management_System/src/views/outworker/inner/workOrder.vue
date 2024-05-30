<!-- 工单列表 -->
<template>
  <div class='work-order'>
    <div class="work-order-table">
      <el-table :data="gridData" style="width: 100%;height: 520px;" fit :border="true">
        <el-table-column prop="name" label="外勤任务标题"/>
        <el-table-column prop="status" label="任务状态"/>
        <el-table-column fixed="right" label="操作" width="120">
          <template #default="{ row }">
            <el-button link type="primary" size="small" plain @click="taskAllocationDetail(row)" :disabled="row.status !== '待处理'">
              详情
            </el-button>
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
      <el-dialog v-model="dialogFormVisible" title="选择是否接受任务" width="500" draggable>
        <div>
          <div>
            <el-form :model="form" label-width="auto" style="max-width: 1000px" class="form-content">
              <el-form-item label="任务名">
                <el-input v-model="form.name"/>
              </el-form-item>
              <el-form-item label="任务简介">
                <el-input v-model="form.taskBrief" type="textarea"/>
              </el-form-item>
            </el-form>
          </div>
          <div class="dialog btn-box">
            <el-button type="primary" @click="dealTask('accept')">接受</el-button>
            <el-button type="primary" @click="dealTask('reject')">拒绝</el-button>
            <el-button type="primary" @click="dialogFormVisible = false">稍后选择</el-button>
          </div>
        </div>
      </el-dialog>
    </div>

  </div>
</template>

<script setup lang='ts'>
import {ref, reactive, onMounted} from 'vue'
import {ElMessage, ElMessageBox} from 'element-plus'
import {Action} from 'element-plus'
import axios from "axios";


/* =------------------------------ */
const getLocation = () => {
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(showPosition, showError);
  } else {
    document.getElementById("location").innerHTML = "Geolocation is not supported by this browser.";
  }

}
const showPosition = (position) => {
  var latitude = position.coords.latitude;
  var longitude = position.coords.longitude;
  document.getElementById("location").innerHTML = "纬度: " + latitude +
      "<br>经度: " + longitude;

  // 你可以在这里将位置信息发送到服务器
  // 示例：通过POST请求发送数据
  fetch('/save-location', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      latitude: latitude,
      longitude: longitude,
      timestamp: new Date().toISOString()
    })
  }).then(response => response.json())
      .then(data => console.log('Success:', data))
      .catch((error) => console.error('Error:', error));
}
const showError = (error) => {
  switch (error.code) {
    case error.PERMISSION_DENIED:
      document.getElementById("location").innerHTML = "用户拒绝了请求地理定位。"
      break;
    case error.POSITION_UNAVAILABLE:
      document.getElementById("location").innerHTML = "位置信息是不可用的。"
      break;
    case error.TIMEOUT:
      document.getElementById("location").innerHTML = "请求获取用户位置超时。"
      break;
    case error.UNKNOWN_ERROR:
      document.getElementById("location").innerHTML = "一个未知错误发生。"
      break;
  }
}

/* =================================== */


// 编辑弹出框
const dialogFormVisible = ref(false)
const formLabelWidth = '140px'


let form = ref({})


const gridData = ref([])
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

function taskAllocationDetail(row) {
  form.value = row;
  dialogFormVisible.value = true
}

function dealTask(flag) {
  const token = localStorage.getItem('token');
  let myParams = new URLSearchParams()
  myParams.append("id", form.value.allocationId);
  let status = 0;
  if (flag === 'accept') {
    status = 1;
  } else {
    status = 2;
  }
  myParams.append("status", String(status));
  axios.put("http://localhost:8080/taskAllocation/dealTaskAllocationInfo",
      myParams,
      {
        headers: {
          'Authorization': `${token}`,
          'Content-Type':'application/x-www-form-urlencoded'
        },
      }).then(res => {
    console.log(res)
    fetchGetTaskAllocationList()
    dialogFormVisible.value = false
  }, err => {
    console.log(err)
  })
}

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
      gridData.value = []
      for (let i = 0; i < result.data.length; i++) {
        await axios.get('http://localhost:8080/fieldTask/getFieldTaskById/' + result.data[i].taskId, {
          headers: {
            'Authorization': `${token}`,
            'Content-Type': 'application/json'
          }
        }).then(res => {
          let task = res.data.data;
          task['allocationId'] = result.data[i].id
          task['status'] = statusChange(result.data[i].status)
          gridData.value.push(task)
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
      return '待处理';
    case 1:
      return '已接受';
    case 2:
      return '已拒绝';
  }
}

onMounted(() => {
  fetchGetTaskAllocationList()
})
</script>

<style lang='scss' scoped>
.work-order-table {
  padding: 20px;
}

.page {
  margin-top: 10px;
}

.btn-box {
  display: flex;
  justify-content: space-around;
}

.form-content {
  padding: 20px;
}

</style>
