<!-- 员工列表 -->
<template>
  <div class='staff'>
    <div>
      <div class="table-header">
        <!-- 表头的三个按钮 -->
        <div>
          <el-button type="primary" plain @click="addVisible = true">
            新增外勤人员
          </el-button>
          <el-button type="danger" plain @click="deleteVisible = true">
            删除外勤人员
          </el-button>
          <el-upload v-model:file-list="fileList" class="upload"
                     action="http://localhost:8080/user/importEmployee"
                     :on-success="fetchEmployees"
                     :on-preview="handlePreview"
                     :on-remove="handleRemove" :before-remove="beforeRemove" :limit="1" :on-exceed="handleExceed"
                     :accept="multipart/form-data" :show-file-list="false">
            <el-button type="primary" plain>导入名单</el-button>
          </el-upload>
        </div>

        <!-- 搜索框 -->
        <div>
          <el-form-item>
            <div width="1000">
              <el-input v-model="search" placeholder="请输入姓名"/>
            </div>
          </el-form-item>
        </div>
      </div>

      <el-table :data="filterTableData" style="width: 100%; height: 480px;" :border="true" fit
                @selection-change="handleSelection">
        <el-table-column type="selection" width="40"/>

        <el-table-column prop="name" label="姓名"/>
        <el-table-column prop="gender" label="性别"/>
        <el-table-column prop="jobNumber" label="工号"/>
        <el-table-column label="所属部门" :filters="departmentFilters"
                         :filter-method="filterHandler">
          <template #default="scope">
            <span>{{ scope.row.department.name }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="taskAmount" label="任务数量"/>
        <el-table-column fixed="right" label="Operations">
          <template #default="{ row }">
            <el-button link type="primary" size="small" plain @click="handleEdit(row)">编辑</el-button>
            <el-button link type="danger" size="small" plain @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="page">
        <el-pagination v-model:current-page="currentPage" v-model:page-size="pageSize" :small="small"
                       :disabled="disabled" :background="background" layout="prev, pager, next, jumper" :total="1000"
                       @size-change="handleSizeChange" @current-change="handleCurrentChange"/>
      </div>
    </div>

    <!-- 编辑弹窗 -->
    <el-dialog v-model="editVisible" title="修改信息" width="400" draggable>
      <el-form :model="form" label-width="auto" style="max-width: 350px" class="form-content">
        <el-form-item label="姓名">
          <el-input v-model="form.name" :disabled="isEditing"/>
        </el-form-item>
        <el-form-item label="性别">
          <el-select v-model="form.gender" placeholder="请选择">
            <el-option label="女" value="女"/>
            <el-option label="男" value="男"/>
          </el-select>
        </el-form-item>
        <el-form-item label="工号">
          <el-input v-model="form.jobNumber" :disabled="isEditing"/>
        </el-form-item>
        <el-form-item label="所属部门">
          <el-select v-model="form.department" value-key="id" placeholder="请选择">
            <el-option v-for="item in departmentList"
                       :key="item.id"
                       :value="item"
                       :label="item.name"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <div class="dialog btn-box">
        <el-button type="primary" @click="handleSave()">确认</el-button>
        <el-button type="primary" @click="editVisible = false">取消</el-button>
      </div>
    </el-dialog>
  </div>

  <!-- 新增员工 -->
  <div>
    <el-dialog v-model="addVisible" title="新增员工" width="500" draggable>
      <div>
        <div>
          <el-form :model="addForm" label-width="auto" style="max-width: 1000px" class="form-content">

            <el-form-item label="姓名">
              <el-input v-model="addForm.name"/>
            </el-form-item>
            <el-form-item label="性别">
              <el-select v-model="addForm.gender">
                <el-option label="男" value="男"/>
                <el-option label="女" value="famale"/>
              </el-select>
            </el-form-item>
            <el-form-item label="所属部门">
              <el-select v-model="addForm.department" value-key="id" placeholder="please select your zone">
                <el-option v-for="item in departmentList"
                           :value="item"
                           :label="item.name"
                />
              </el-select>
            </el-form-item>
          </el-form>
        </div>
        <div class="dialog btn-box">
          <el-button type="primary" @click="employeeAdd">新增</el-button>
          <el-button type="primary" @click="addVisible = false">取消</el-button>
        </div>
      </div>
    </el-dialog>
  </div>

  <!-- 删除人员 -->
  <div>
    <el-dialog v-model="deleteVisible" title="确认删除" width="400" :before-close="handleClose" center>
      <div style="padding-left:20px;">
        确认删除所选员工吗？
      </div>
      <template #footer>
        <div class="dialog-footer btn-box">
          <el-button type="primary" @click="allEmployeeDelete">
            确认
          </el-button>
          <el-button type="primary" @click="deleteVisible = false">取消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang='ts'>
import {computed, onMounted, reactive, ref} from 'vue'
import {ElMessage, ElMessageBox} from 'element-plus'
import axios from "axios";
// 导入员工名单
const handleRemove = (file, uploadFiles) => {
  console.log(file, uploadFiles)
}

const handlePreview = (uploadFile) => {
  console.log(uploadFile)
}

const handleExceed = (files, uploadFiles) => {
  ElMessage.warning(
      `The limit is 3, you selected ${files.length} files this time, add up to ${files.length + uploadFiles.length
      } totally`
  )
}

const beforeRemove = (uploadFile, uploadFiles) => {
  return ElMessageBox.confirm(
      `Cancel the transfer of ${uploadFile.name} ?`
  ).then(
      () => true,
      () => false
  )
}

// 编辑弹出框
const isEditing = ref(false); // 标志是否处于编辑模式
const editVisible = ref(false)
const addVisible = ref(false)
const deleteVisible = ref(false)

let selectionList = ref([])
let fileList = ref([])

// 搜索框
const search = ref('')
const filterTableData = computed(() =>
    tableData.value.filter(
        (data) =>
            !search.value ||
            data.name.includes(search.value)
    )
)

let form = reactive({
  id: '',
  identity: '',
  name: '',
  gender: '',
  jobNumber: '',
  department: {
    id: '',
    name: ''
  },
  taskAmount: '',
})

let addForm = reactive({
  name: '',
  gender: '',
  department: {
    id: '',
    name: ''
  },
})

// 处理编辑按钮点击事件
const handleEdit = (row) => {
  // 将所选行的数据填充到编辑表单中
  form.id = row.id
  form.name = row.name
  form.gender = row.gender
  form.department = row.department
  form.jobNumber = row.jobNumber
  editVisible.value = true; // 打开编辑弹窗
  isEditing.value = true; // 将编辑模式设置为 true
};

function allEmployeeDelete() {
  fetchDeleteEmployees()
}

function handleSelection(value) {
  selectionList.value = value;
}

function employeeAdd() {
  fetchAddEmployee()
}

const fetchDeleteEmployees = async () => {
  const token = localStorage.getItem('token');
  let ids = [];
  for (let i = 0; i < selectionList.value.length; i++) {
    ids.push(selectionList.value[i].id)
  }
  axios({
    method: 'delete',
    url: 'http://localhost:8080/user/deleteListByIds',
    headers: {
      'Authorization': `${token}`,
      'Content-Type': 'application/json'
    },
    data: ids
  }).then(res => {
    deleteVisible.value = false;
    fetchEmployees()
  }, err => {
    console.log(err);
  })
}
const fetchAddEmployee = async () => {
  try {
    const token = localStorage.getItem('token');
    const employee = {
      name: addForm.name,
      gender: addForm.gender,
      department: addForm.department.id
    }
    const res = await fetch('http://localhost:8080/user/addUser', {
      method: 'POST',
      headers: {
        'Authorization': `${token}`,
        'Content-Type': 'application/json'
      },

      body: JSON.stringify(employee)
    });
    const result = await res.json();
    if (result.code === 200) {
      await fetchEmployees()
      addVisible.value = false
    } else {
      console.error('Failed to fetch addEmployee:', result.message);
    }
  } catch (error) {

  }
}
const handleSave = async () => {
  try {
    const token = localStorage.getItem('token');
    const baseUrl = 'http://localhost:8080/user/updateEmployee';
    const requestBody = {
      id: form.id,
      gender: form.gender,
      department: form.department.id
    };
    const res = await fetch('http://localhost:8080/user/updateEmployee', {
      method: 'PUT',
      headers: {
        'Authorization': `${token}`,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(requestBody)
    });
    const result = await res.json();
    if (result.code === 200) {
      await fetchEmployees()
      editVisible.value = false;
      form = {
        id: '',
        identity: '',
        name: '',
        gender: '',
        jobNumber: '',
        department: {
          id: '',
          name: ''
        },
        taskAmount: '',
      }
    } else {
      console.error('Failed to fetch departments:', result.message);
    }
  } catch (error) {

  }
}

function handleDelete(row) {
  fetchDeleteEmployee(row)
}

// 表筛选
const departmentFilters = ref([]);

const departmentList = ref([]);

const departments = ref([])

const fetchDepartments = async () => {
  try {
    const token = localStorage.getItem('token');
    const response = await fetch('http://localhost:8080/department/getDepartments', {
      method: 'GET',
      headers: {
        'Authorization': `${token}`,
        'Content-Type': 'application/json'
      }
    });
    const result = await response.json();

    if (result.code === 200) {

      // 提取部门列表并设置部门过滤器
      const departmentsArray = Array.from(result.data);
      console.log(departmentsArray)
      departmentList.value = departmentsArray
      departmentFilters.value = departmentsArray.map(dept => ({
        text: dept.name,
        value: dept.name
      }));
    } else {
      console.error('Failed to fetch departments:', result.message);
    }
  } catch (error) {
    console.error('Error fetching departments:', error);
  }
};

const filterHandler = (value, row) => {
  return row.department === value;
};

const tableData = ref([]);

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
      tableData.value = result.data;
      console.log(tableData.value)
      for (let i = 0; i < tableData.value.length; i++) {
        tableData.value[i].department = departmentList.value.find(item => item.id == tableData.value[i].department)
      }
    } else {
      ElMessage.error(`Failed to fetch employees: ${result.message}`);
    }
  } catch (error) {
    ElMessage.error('Error fetching employees');
    console.error('Error fetching employees:', error);
  }
};
const fetchDeleteEmployee = async (row) => {
  try {
    const token = localStorage.getItem('token');
    const response = await fetch('http://localhost:8080/user/deleteOneById/' + row.id, {
      method: 'DELETE',
      headers: {
        'Authorization': `${token}`,
        'Content-Type': 'application/json'
      }
    });
    const result = await response.json();

    if (result.code === 200) {
      ElMessage.success('删除员工成功')
    } else {
      ElMessage.error(`Failed to fetch employees: ${result.message}`);
    }
  } catch (error) {
    ElMessage.error('Error fetching employees');
    console.error('Error fetching employees:', error);
  }
};
onMounted(() => {
  fetchEmployees();
  fetchDepartments();
});

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
.page {
  margin-top: 10px;
}

.upload {
  display: inline-block;
  margin-left: 13px;
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
</style>
