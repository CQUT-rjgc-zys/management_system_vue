<!-- 部门管理 -->
<template>
    <div class='work-order'>
        <div class="work-order-table">
            <div class="table-header">
                <div>
                    <el-button type="primary" plain @click="addFormVisible = true">
                        新增部门信息
                    </el-button>
                </div>
                <div>
                    <el-form-item>
                        <div width="1000">
                            <el-input v-model="search" placeholder="请输入部门名" />
                        </div>
                    </el-form-item>
                </div>
            </div>
            <el-table :data="filterTableData" style="width: 100%;height: 480px;" fit :border="true"
                @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="40" />
                <el-table-column prop="id" label="随机编号" />
                <el-table-column prop="name" label="部门" />
                <el-table-column fixed="right" label="操作" width="120">
                    <template #default="{ row }">
                        <el-button link type="danger" size="small" plain @click="deleteDepartmentDialog(row)">
                            删除
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="page">
                <el-pagination v-model:current-page="currentPage" v-model:page-size="pageSize" :small="small"
                    :disabled="disabled" :background="background" layout="prev, pager, next, jumper" :total="1000"
                    @size-change="handleSizeChange" @current-change="handleCurrentChange" />
            </div>
        </div>

        <!-- draggable 可移动 -->
        <!-- 新增弹窗 -->
        <el-dialog v-model="addFormVisible" title="新增部门" width="400">
            <div>
                <el-form :model="form" label-width="auto" style="max-width: 1000px" class="form-content">
                    <el-form-item label="部门">
                        <el-input v-model="form.name" />
                    </el-form-item>
                </el-form>
                <div class="dialog btn-box">
                    <el-button type="primary" @click="addDepartment">新增</el-button>
                    <el-button type="primary" @click="addFormVisible = false">取消</el-button>
                </div>
            </div>
        </el-dialog>

        <!-- 删除弹窗 -->
        <div>
            <el-dialog v-model="deleteFormVisible" title="确认删除" width="400" :before-close="handleClose" center>
                <div style="padding-left: 20px;">
                    确认删除所选部门吗？
                </div>
                <template #footer>
                    <div class="dialog-footer btn-box">
                        <el-button type="primary" @click="deleteDepartment()">
                            确认
                        </el-button>
                        <el-button type="primary" @click="deleteFormVisible = false">取消</el-button>

                    </div>
                </template>
            </el-dialog>
        </div>
    </div>
</template>

<script setup lang='ts'>
import { ref, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import axios from "axios";

// 编辑弹出框
const addFormVisible = ref(false)
const deleteFormVisible = ref(false)

// 搜索框
const search = ref('')

const filterTableData = computed(() =>
    tableData.value.filter(
        (data) =>
            !search.value ||
            data.name.includes(search.value)
    )
);

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

const form = ref({
    name: ''
});

const tableData = ref([])

let deleteForm = ref({})

onMounted(async () => {
    await fetchDepartments();
});

function deleteDepartmentDialog(row) {
  deleteForm.value = row
  deleteFormVisible.value = true
}

function deleteDepartment() {
  const token = localStorage.getItem('token');
  axios({
    url: 'http://localhost:8080/department/deleteDepartment',
    method: 'delete',
    headers: {
      'Authorization': `${token}`,
      'Content-Type': 'application/x-www-form-urlencoded'
    },
    params: {
      id: deleteForm.value.id
    }
  }).then(res => {
    fetchDepartments()
    deleteFormVisible.value = false
  }, err => {
    console.log(err)
    ElMessage.error(err.response.data.message)
    deleteFormVisible.value = false
  })
}

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
            tableData.value = result.data;
        } else {
            console.error('Failed to fetch departments:', result.message);
        }
    } catch (error) {
        console.error('Error fetching departments:', error);
    }
};

const addDepartment = async () => {
    try {
        const token = localStorage.getItem('token');
        const response = await fetch('http://localhost:8080/department/addDepartment', {
            method: 'POST',
            headers: {
                'Authorization': `${token}`,
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: new URLSearchParams({
                name: form.value.name
            })
        });
        const result = await response.json();

        if (result.code === 200) {
            ElMessage.success('Department added successfully');
            addFormVisible.value = false;
            form.value.name = '';
            await fetchDepartments(); // 重新获取部门数据
        } else {
            ElMessage.error(`Failed to add department: ${result.message}`);
        }
    } catch (error) {
        ElMessage.error('Error adding department');
        console.error('Error adding department:', error);
    }
};

</script>

<style lang='scss' scoped>
.page {
    margin-top: 10px;
}

.table-header {
    display: flex;
    justify-content: space-between;
}

.btn-box {
    display: flex;
    justify-content: space-around;

    button {
        width: 100px;
    }
}

.form-content {
    padding: 20px;
}
</style>
