<!-- 外勤任务打卡管理 -->
<template>
  <div class='task-report'>
    <div>
      <el-form-item class="table-header">
        <div>
          <el-input v-model="search" placeholder="请输入任务名" />
        </div>
      </el-form-item>
      <el-table :data="filterTableData" style="width: 100%; height: 480px" fit :border="true">
        <el-table-column prop="name" label="外勤任务标题" />
        <el-table-column prop="status" label="任务状态" sortable column-key="status" :filters="[
          { text: '未完成', value: '未完成' },
          { text: '已完成', value: '已完成' },
        ]" :filter-method="filterHandler" />
        <el-table-column fixed="right" label="打卡情况" width="200">
          <template #default>
            <el-button link type="primary" size="small"  @click="detail">
              点击查看
            </el-button>
            <!-- <el-button link type="primary" size="small" plain @click="editVisible = true">编辑</el-button> -->
          </template>
        </el-table-column>
      </el-table>
      <div class="page">
        <el-pagination v-model:current-page="currentPage" v-model:page-size="pageSize" :small="small"
          :disabled="disabled" :background="background" layout="prev, pager, next, jumper" :total="1000"
          @size-change="handleSizeChange" @current-change="handleCurrentChange" />
      </div>
    </div>
  </div>
</template>

<script setup lang='ts'>
import { ref, reactive, onMounted, getCurrentInstance } from 'vue'
import {computed} from "vue";

import { useRouter } from "vue-router";
import {ElMessage} from "element-plus";

// 打卡管理界面
const router = useRouter();
const detail = () => {
  router.push("reportMana");
}

// 搜索框
const search = ref('')
const filterTableData = computed(() =>
  tableData.filter(
    (data) =>
      !search.value ||
      data.name.toLowerCase().includes(search.value.toLowerCase())
  )
)


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
  status: '',
})

const tableData = [
  {
    name: '和**合作',
    status: '未完成',
  },
  {
    name: '和**合作',
    status: '进行中',
  },
  {
    name: '和**合作',
    status: '待确认',
  },
  {
    name: '和**合作',
    status: '待确认',
  },
]

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


onMounted(() => {

})


</script>

<style lang='scss' scoped>

.page {
  margin-top: 10px;
}

.table-header {
  float: right;
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
  padding-left: 100px;
}

.uploadbtn {
  margin-left: 40px;
}
</style>
