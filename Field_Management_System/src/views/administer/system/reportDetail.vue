<!-- 打卡考勤 -->
<template>
  <div class='reportmana'>
    <el-page-header @back="goBack">
      <template #content>
        <span class="text-large font-600 mr-3"> 和**谈合作 </span>
      </template>
    </el-page-header>
    <div class="echart">
      <div class="left-pie">
        <div id='pieCharts' ref="pieCharts"></div>
      </div>
      <div class="right-bar">
        <div id="barCharts" ref="barCharts">
        </div>
      </div>
    </div>

    <div class="report-table">
      <h3>今日人员打卡情况</h3>
      <el-table :data="tableData" style="width: 100%;" fit :border="true">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="name" label="姓名" />
        <el-table-column prop="jobNumber" label="工号" />
        <el-table-column prop="taskName" label="外勤任务标题" />
        <el-table-column prop="time" label="打卡时间" />
        <el-table-column prop="spot" label="打卡地点" />
        <el-table-column fixed="right" label="定位" width="100">
            <el-button link type="primary" size="small" @click="detail">
              点击查看
            </el-button>
        </el-table-column>
        <el-table-column fixed="right" label="操作" width="100">
          <template #default>
            <el-button link type="primary" size="small" @click="remindVisible = true">
              提醒
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="page">
        <el-pagination v-model:current-page="currentPage" v-model:page-size="pageSize" :small="small"
          :disabled="disabled" :background="background" layout="prev, pager, next, jumper" :total="1000" />
      </div>
    </div>

    <!-- 提示消息弹窗 -->
    <div>
      <el-dialog v-model="remindVisible" title="提示消息" width="400" draggable>
        <div>
          <el-form :model="form" label-width="auto" class="form-content">
            <el-form-item label="提示信息">
              <el-input v-model="form.remind" type="textarea"></el-input>
            </el-form-item>
          </el-form>
        </div>
        <template #footer>
          <div class="dialog-footer">
            <el-button type="primary" @click="remindVisible = false">发送提示信息</el-button>
          </div>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue';


// 返回任务信息列表
const router = useRouter();
const goBack = () => {
  router.push("taskReport");
}
// 定位管理界面
const detail = () => {
  router.push("map");
}

// 编辑弹出框
const remindVisible = ref(false);

const form = reactive({
  remind: '',
  name: '',
  jobNumber: '',
  taskAmount: '',
});

const tableData = [
  {
    name: '叶舒华',
    jobNumber: '12023020201',
    taskName: '与**签约',
  },
  {
    name: '叶舒华',
    jobNumber: '12023020201',
    taskName: '与**签约',
  },
  {
    name: '叶舒华',
    jobNumber: '12023020201',
    taskName: '与**签约',
  },
  {
    name: '叶舒华',
    jobNumber: '12023020201',
    taskName: '与**签约',
  },
];

// 分页
const currentPage = ref(1);

const pageSize = ref(100);

const small = ref(false);
const background = ref(false);
const disabled = ref(false);
/* -------------------------- */
const { proxy } = getCurrentInstance();
// 左边
const pieCharts = ref(); // dom实例
let leftChart = null; // echarts实例

// 右边
const barCharts = ref()
let rightChart = null

onMounted(() => {
  rChart();
  lChart();
})

// 饼状图数据
const pieData = [
  { value: 78, name: '按时打卡' },
  { value: 15, name: '未按时打卡' },
  { value: 34, name: '未打卡' },
]

let lChart = () => {
  leftChart = proxy.$echarts.init(pieCharts.value)

  leftChart.setOption({
    // animation: true,  //动画效果默认为true
    title: {
      text: '打卡情况',
      left: 'center'
    },
    tooltip: {
      trigger: 'item'
    },
    legend: { // 对图形的解释部分
      // 图例
      orient: 'vertical',
      left: 15,
      y: 'center',
      icon: 'circle',
    },
    series: [
      {
        name: '今日打卡情况',
        type: 'pie',
        radius: ['30%', '60%'],
        center: ['55%', '55%'],
        avoidLabelOverlap: false,
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: '20',
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: pieData
      }
    ],
    
  });
}

let rChart = () => {
  rightChart = proxy.$echarts.init(barCharts.value)

  rightChart.setOption({
    title: {
      text: '今日打卡情况'
    },
    tooltip: {},
    legend: {
      data: ['出勤']
    },
    xAxis: {
      data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
    },
    yAxis: {},
    series: [
      {
        name: '出勤',
        type: 'bar',
        data: [5, 20, 36, 10, 10, 20, 2],
      }
    ],
    /* color: [
      '#dd6b66',
      '#759aa0',
      '#e69d87',
      '#8dc1a9',
      '#ea7e53',
      '#eedd78',
      '#73a373',
      '#73b9bc',
      '#7289ab',
      '#91ca8c',
      '#f49f42'
    ], */
  });
}


</script>

<style lang='scss' scoped>
#main {
  height: auto;
  margin-top: 20px;
}
.echart {
 
 display: flex;
 flex-direction: row;
 justify-content: space-around;
}

.left-pie,
.right-bar {
 padding: 30px 10px 0 0;
 width: 400px;
 height: 300px;

 #pieCharts,
 #barCharts {
   width: 400px;
   height: 300px;
 }
}

.report-table {
  margin: 0px 40px;
  padding: 0;
}

.page {
  margin-top: 10px;
}
</style>