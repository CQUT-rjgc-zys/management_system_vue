<!-- 外勤任务详细信息 -->
<template>
  <div class='taskdetail'>
    <el-page-header @back="goBack">
      <template #content>
        <span class="text-large font-600 mr-3"> 外勤任务详细界面 </span>
      </template>
    </el-page-header>
    <div>
      <div class="form-content">
        <el-descriptions :title="task.name" :column="1">
          <el-descriptions-item label="开始时间" width="100">
            {{ formatDateTime(task.startTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="截止日期" width="100">
            {{ formatDateTime(task.endTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="任务地点">
            {{ task.taskSpot }}
            <div id="spotMap" style="margin-top:20px;"></div>
          </el-descriptions-item>
          <el-descriptions-item label="任务状态">
            <el-tag size="small" v-if="task.status === 0">待完善</el-tag>
            <el-tag size="small" v-else-if="task.status === 1">进行中</el-tag>
            <el-tag size="small" v-else-if="task.status === 2">已完成</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="任务简介">
            {{ task.taskBrief }}
          </el-descriptions-item>
          <el-descriptions-item label="任务详情">
            {{ task.description }}
          </el-descriptions-item>
        </el-descriptions>
        <div style="float: left; margin-bottom: 10px">
          <el-button type="primary" plain @click="downloadFile">
            下载相关文件
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang='ts'>
import {ref, reactive, onMounted} from 'vue'
import {ElTable} from 'element-plus';
import {useRouter} from "vue-router";
import AMapLoader from "@amap/amap-jsapi-loader";
import axios from "axios";


// 返回外勤任务列表
const router = useRouter();
const goBack = () => {
  router.push("myTaskList");
}
const taskId = router.currentRoute.value.query.id;
let task = ref({})
// 显示地图
let map = null;

function downloadFile() {
  const token = localStorage.getItem('token');
  axios.get("http://localhost:8080/fieldTask/downloadFile/" + taskId, {
    headers: {
      'Authorization': `${token}`,
      'responseType': 'blob'
    }
  }).then(res => {
    let blob = new Blob([res.data.data])
    if (blob.size > 0) {
      //生成一个a连接
      let tag = document.createElement( "a" );
      //将返回的数据生成url赋值给a链接
      tag.href = URL.createObjectURL( blob );
      //此处规定要下载的文件名称，带文件后缀，要不然可能下载出错
      tag.download = task.value.fileName;
      //模拟按钮点击下载
      tag.click();
      //下载后释放url对象
      URL.revokeObjectURL( tag.href );
    }
  })
}

function taskGetOne() {
  const token = localStorage.getItem('token');
  axios.get("http://localhost:8080/fieldTask/getFieldTaskById/" + taskId,
      {
        headers: {
          'Authorization': `${token}`,
          'Content-Type': 'application/json'
        }
      }).then(res => {
    task.value = res.data.data
    console.log(task)
  }, err => {
    console.log(err)
  })
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
        map = new AMap.Map("spotMap", {
          // 设置地图容器id
          viewMode: "3D",
          zoom: 10,
          center: [106.397428, 29.90923],
          resizeEnable: true,
          terrain: true, //开启地形图
        });
      })
      .catch((e) => {
        console.log(e);
      });
});

onMounted(() => {
  map?.destroy();
  taskGetOne()
});


</script>

<style lang='scss' scoped>
.form-content {
  padding: 20px;
}

#spotMap {
  width: 600px;
  height: 200px;
}
</style>
