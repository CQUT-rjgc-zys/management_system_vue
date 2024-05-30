<!-- 外勤任务详细信息 -->
<template>
  <div class='taskdetail'>
    <el-page-header @back="goBack">
      <template #content>
        <div style="display: flex">
          <span class="text-large font-600 mr-3" style="margin-right: 10px"> 外勤任务详细界面 </span>
          <el-button type="primary" v-if="updateActive" @click="updateStart">编辑</el-button>
          <div v-else-if="!updateActive">
            <el-button @click="updateBack">取消</el-button>
            <el-button type="primary" @click="taskUpdate">确认</el-button>
          </div>
        </div>
      </template>
    </el-page-header>
    <div>
      <div class="form-content">
        <el-form :model="form" label-width="auto" style="max-width: 600px">
          <el-form-item label="任务名">
            <el-input v-model="form.name" :disabled="updateActive"/>
          </el-form-item>
          <el-form-item label="任务时间">
            <el-col :span="11">
              <el-date-picker
                  v-model="form.startTime"
                  type="date"
                  placeholder="开始时间"
                  style="width: 100%"
                  :disabled="updateActive"
              />
            </el-col>
            <el-col :span="2" class="text-center">
              <span class="text-gray-500">-</span>
            </el-col>
            <el-col :span="11">
              <el-date-picker
                  v-model="form.endTime"
                  placeholder="截止时间"
                  style="width: 100%"
                  :disabled="updateActive"
              />
            </el-col>
          </el-form-item>
          <el-form-item label="任务简介">
            <el-input v-model="form.taskBrief" :disabled="updateActive"/>
          </el-form-item>
          <el-form-item label="任务详情">
            <el-input v-model="form.description" type="textarea" :disabled="updateActive"/>
          </el-form-item>
          <el-form-item label=" ">
            <div style="display: flex">
              <el-upload v-model:file-list="fileList" class="upload"
                         action="#"
                         :headers="header"
                         :limit="1"
                         :http-request="uploadFile"
                         :show-file-list="false">
                <el-button type="primary" plain>上传相关文件</el-button>
              </el-upload>
              <div style="margin-left: 20px">{{ form.fileName }}</div>
            </div>
          </el-form-item>
          <el-form-item label="任务地点">
            <el-input id="tipinput" ref="taskSpot" v-model="form.taskSpot" style="width: 450px;margin-right: 20px;"
                      :disabled="updateActive"/>
            <el-button type="primary" v-if="submit" @click="submitOn" :disabled="updateActive">确认</el-button>
            <el-button type="primary" v-show="edit" @click="submitOn" :disabled="updateActive">修改</el-button>
            <div id="taskSpotMap" style="margin-top:20px;"></div>
          </el-form-item>

        </el-form>
        <div style="float: left; margin-bottom: 10px">
          <el-button type="primary" plain @click="issueVisible = true">
            分配外勤人员
          </el-button>
        </div>
      </div>
      <el-table title="外勤人员" :data="tableData" style="width: 100%;margin-left: 20px;" :fit="true" :border="true">
        <el-table-column prop="name" label="外勤人员姓名"/>
        <el-table-column prop="gender" label="性别"/>
        <el-table-column prop="jobNumber" label="工号"/>
        <el-table-column fixed="right" label="操作">
          <template #default="{ row }">
            <el-button link type="danger" size="small" plain @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <!--      <div class="page">-->
      <!--        <el-pagination v-model:current-page="currentPage" v-model:page-size="pageSize" :small="small"-->
      <!--                       :disabled="disabled" :background="background" layout="prev, pager, next, jumper" :total="1000"-->
      <!--                       @size-change="handleSizeChange" @current-change="handleCurrentChange"/>-->
      <!--      </div>-->
    </div>

    <!-- 分配外勤人员弹窗 -->
    <div>
      <el-dialog v-model="issueVisible" title="分配外勤人员" width="450" draggable center>
        <div class='form'>
          <el-form :model="employeeForm" label-width="auto" class="form-content">
            <el-form-item label="分配外勤人员">
              <el-select v-model="employeeForm.employeeList" multiple filterable value-key="id"
                         placeholder="请选择外勤合适的外勤人员">
                <el-option v-for="item in userList" :key="item.id" :label="item.name" :value="item"/>
              </el-select>
            </el-form-item>
          </el-form>
        </div>
        <template #footer>
          <div class="dialog-footer btn-box">
            <el-button type="primary" @click="taskAllocationAdd">
              确认
            </el-button>
            <el-button type="primary" @click="issueVisible = false">取消</el-button>
          </div>
        </template>
      </el-dialog>
    </div>


  </div>
</template>

<script setup lang='ts'>
import {ref, reactive, onMounted, toRefs} from 'vue'
import {useRouter} from "vue-router";
import AMapLoader from "@amap/amap-jsapi-loader";
import {Search} from '@element-plus/icons-vue';
import {ElMessage} from "element-plus";
import axios from "axios";
// 返回外勤任务列表
const router = useRouter();
const goBack = () => {
  router.push("taskList");
}
const taskId = router.currentRoute.value.query.taskId
const baseToken = localStorage.getItem("token")
// 编辑弹出框
let issueVisible = ref(false)
const formLabelWidth = '140px'

let updateActive = ref(true)

// 分页
const currentPage = ref(1)
const pageSize = ref(100)
// const small = ref(false)
// const background = ref(false)
// const disabled = ref(false)

// 切换按钮
const taskSpot = ref(null);
console.log(taskSpot.value);
let submit = ref(true);
let edit = ref(false);

const submitOn = (data) => {
  console.log(data);
  if (taskSpot.value) {
    console.log(taskSpot.value);
    edit.value = true;
    submit.value = false;
  } else {
    edit.value = false;
    submit.value = true
  }
}


// option多选框
let userList = ref([])
let allUser = ref([])

let fileList = ref([])
const header = ref({
  'Authorization': `${baseToken}`,
  'Content-Type': 'multipart/form-data'
})

let form = ref({
  id: '',
  name: '',
  startTime: '',
  endTime: '',
  taskBrief: '',
  description: '',
  taskSpot: '',
  fileName: ''
})

let copyForm = ref({
  id: '',
  name: '',
  startTime: '',
  endTime: '',
  taskBrief: '',
  description: '',
  taskSpot: ''
})

let employeeForm = ref({
  employeeList: []
})

const tableData = ref([])


// 任务地点内置地图
let map = null;

function handleDelete(row) {
  fetchDeleteEmployee(row)
}

function updateBack() {
  form.value = copyForm.value
  updateActive.value = true
}

function updateStart() {
  copyForm.value = {...form.value};
  updateActive.value = false
}

function taskUpdate() {
  fetchUpdateTask()
}

function taskAllocationAdd() {
  issueVisible.value = true
  fetchAddTaskAllocation(taskId, employeeForm.value.employeeList);
}

function uploadFile() {
  const token = localStorage.getItem('token');
  let formData = new FormData();
  formData.append("file", fileList.value[0].raw)
  axios.post("http://localhost:8080/fieldTask/uploadFile/" + taskId, formData, {
    headers: {
      'Authorization': `${token}`,
      'Content-Type': 'multipart/form-data'
    }
  }).then(res => {
    form.value.fileName = fileList.value[0].name
  }, err => {
    console.log(err)
  })
}

const fetchDeleteEmployee = async (row) => {
  try {
    const token = localStorage.getItem('token');
    const response = await fetch('http://localhost:8080/taskAllocation/deleteAllocationInfo/' + row.id + '/' + taskId, {
      method: 'DELETE',
      headers: {
        'Authorization': `${token}`,
        'Content-Type': 'application/json'
      }
    });
    const result = await response.json();

    if (result.code === 200) {
      await fetchGetUsersByFieldTaskId()
      userList.value.push(row)
      ElMessage.success('删除员工成功')
    } else {
      ElMessage.error(`Failed to fetch employees: ${result.message}`);
    }
  } catch (error) {
    ElMessage.error('Error fetching employees');
    console.error('Error fetching employees:', error);
  }
};
const fetchUpdateTask = async () => {
  copyForm.value = form.value
  const token = localStorage.getItem('token');
  if (typeof taskId === "string") {
    form.value.id = taskId
  }
  axios({
    method: 'put',
    url: 'http://localhost:8080/fieldTask/updateFieldTask',
    headers: {
      'Authorization': `${token}`,
      'Content-Type': 'application/json'
    },
    data: form.value
  }).then(res => {
    updateActive.value = true
  }, err => {
    console.log(err);
  })
}
const fetchGetFieldTaskById = async () => {
  try {
    const token = localStorage.getItem('token');
    const response = await fetch('http://localhost:8080/fieldTask/getFieldTaskById/' + taskId, {
      method: 'GET',
      headers: {
        'Authorization': `${token}`,
        'Content-Type': 'application/json'
      }
    });
    const result = await response.json();

    if (result.code === 200) {
      form.value = result.data
      console.log(result.data)
    } else {
      ElMessage.error(`Failed to fetch employees: ${result.message}`);
    }
  } catch (error) {
    ElMessage.error('Error fetching taskDetail');
    console.error('Error fetching taskDetail:', error);
  }
}

const fetchGetUsersByFieldTaskId = async () => {
  try {
    const token = localStorage.getItem('token');
    const response = await fetch('http://localhost:8080/user/getUsersByFiledTaskId/' + taskId, {
      method: 'GET',
      headers: {
        'Authorization': `${token}`,
        'Content-Type': 'application/json'
      }
    });
    const result = await response.json();

    if (result.code === 200) {
      tableData.value = []
      if (result.data['0'] !== null) {
        allUser.value = [...allUser.value, ...result.data['0']]
      }
      if (result.data['1'] !== null) {
        allUser.value = [...allUser.value, ...result.data['1']]
        tableData.value = [...tableData.value, ...result.data['1']]
      }
      if (result.data['2'] !== null) {
        allUser.value = [...allUser.value, ...result.data['2']]
      }
    } else {
      ElMessage.error(`Failed to fetch employees: ${result.message}`);
    }
  } catch (error) {
    ElMessage.error('Error fetching taskDetail');
    console.error('Error fetching taskDetail:', error);
  }
}

const fetchEmployees = async () => {
  try {
    const token = localStorage.getItem('token');
    const response = await fetch('http://localhost:8080/user/getAllEmployee', {
      method: 'GET',
      headers: {
        'Authorization': `${token}`,
        'Content-Type': 'application/json'
      }
    });
    const result = await response.json();

    if (result.code === 200) {
      userList.value = result.data;
      for (let i = 0; i < allUser.value.length; i++) {
        userList.value = userList.value.filter(item => item.id !== allUser.value[i].id)
      }
    } else {
      ElMessage.error(`Failed to fetch employees: ${result.message}`);
    }
  } catch (error) {
    ElMessage.error('Error fetching employees');
    console.error('Error fetching employees:', error);
  }
};

const fetchAddTaskAllocation = async (taskId, employeeList) => {
  const token = localStorage.getItem('token');
  const taskAllocationList = ref([])
  for (let i = 0; i < employeeList.length; i++) {
    taskAllocationList.value.push({
      id: null,
      taskId,
      userId: employeeList[i].id,
      status: 0
    })
  }
  axios({
    method: 'post',
    url: 'http://localhost:8080/taskAllocation/addAllocationInfos',
    headers: {
      'Authorization': `${token}`,
      'Content-Type': 'application/json'
    },
    data: JSON.stringify(taskAllocationList.value)
  }).then(res => {
    for (let i = 0; i < employeeList.length; i++) {
      userList.value = userList.value.filter(item => item.id !== employeeList[i].id)
    }
    employeeForm.value.employeeList = []
    issueVisible.value = false;
  }, err => {
    console.log(err);
  })

  // try {
  //   const token = localStorage.getItem('token');
  //   const taskAllocationList = ref([])
  //   for (let i = 0; i < userList.length; i++) {
  //     taskAllocationList.value.push({
  //       taskId,
  //       userId: userList[i]
  //     })
  //   }
  //   const response = await fetch('http://localhost:8080/taskAllocation/addAllocationInfos', {
  //     method: 'POST',
  //     headers: {
  //       'Authorization': `${token}`,
  //       'Content-Type': 'application/json'
  //     },
  //     body: {
  //       param: taskAllocationList.value
  //     }
  //   });
  //   const result = await response.json();
  //
  //   if (result.code === 200) {
  //     console.log(result.data)
  //   } else {
  //     ElMessage.error(`Failed to fetch taskAllocation: ${result.message}`);
  //   }
  // } catch (error) {
  //   ElMessage.error('Error fetching taskAllocation');
  //   console.error('Error fetching taskAllocation:', error);
  // }
};

onMounted(() => {
  fetchGetFieldTaskById()
  fetchGetUsersByFieldTaskId()
  setTimeout(() => {
    fetchEmployees()
  }, 1000)


  window._AMapSecurityConfig = {
    securityJsCode: "3bb27e8a128223c35f43cf5e04a8fedf",
  };

  // 加载js API
  AMapLoader.load({
    key: "47e625684e574f55150900888756c8c0", // 申请好的Web端开发者Key，首次调用 load 时必填
    version: "2.0",
    plugins: ["AMap.PlaceSearch", "AMap.AutoComplete"]
  })
      .then(AMap => {
        map = new AMap.Map("taskSpotMap", {
          // 设置地图容器id
          viewMode: "3D",
          zoom: 10,
          center: [106.397428, 29.90923],
          resizeEnable: true,
          terrain: true, //开启地形图
        });
        const autoOptions = {
          input: "tipinput"
        };
        const auto = new AMap.AutoComplete(autoOptions);
        const placeSearch = new AMap.PlaceSearch({
          map: map
        });  //构造地点查询类
        auto.on("select", select);//注册监听，当选中某条记录时会触发
        function select(e) {
          placeSearch.setCity(e.poi.adcode);
          placeSearch.search(e.poi.name);  //关键字查询查询
        }

      })
      .catch((e) => {
        console.log(e);
      });
});

onMounted(() => {
  map?.destroy();
});


</script>

<style lang='scss' scoped>
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
  float: right;
}

.form-content {
  padding: 20px;
}

#taskSpotMap {
  /* background-color: #5c5555; */
  width: 600px;
  height: 200px;
}
</style>
