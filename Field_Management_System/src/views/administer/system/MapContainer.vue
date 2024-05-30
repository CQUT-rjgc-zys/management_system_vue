<!-- 定位详情 -->
<template>
  <div>
    <el-page-header @back="goBack">
      <template #content>
        <span class="text-large font-600 mr-3"> 和**谈合作 </span>
      </template>
    </el-page-header>
    <div id="container">

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

<script setup lang='ts'>
  import { ref, reactive, onMounted, onUnmounted } from 'vue';
  import AMapLoader from "@amap/amap-jsapi-loader";

  // 编辑弹出框
  const remindVisible = ref(false);


  // 返回定位打卡列表
  const router = useRouter();
  const goBack = () => {
    router.push("reportMana");
  }


  let map = null;

  onMounted(() => {
    window._AMapSecurityConfig = {
      securityJsCode: "3bb27e8a128223c35f43cf5e04a8fedf", //申请的安全密钥
    };
    // 加载js API
    AMapLoader.load({
      key: "47e625684e574f55150900888756c8c0", // 申请好的Web端开发者Key
      version: "2.0", // 指定要加载的 JSAPI 的版本
      plugins: ["AMap.ToolBar", "AMap.Scale", "AMap.MapType"],
    })
      .then((AMap) => {
        // 创建地图对象 设置地图容器id
        map = new AMap.Map("container", {
          center: [106.791541, 29.712158], // 初始化地图中心点位置
          // mapStyle: "amap://styles/whitesmoke", //地图的显示样式
          viewMode: "3D", // 是否为3D地图模式
          zoom: 11, // 初始化地图级别
          // pitch: 45, //角度
          terrain: true, //开启地形图

        });
        //异步同时加载多个插件
        
        map.addControl(new AMap.MapType()); // 实现默认图层与卫星图,实时交通图层之间切换
        const tool = new AMap.ToolBar();
        map.addControl(tool); // 工具条控件;范围选择控件
        const scale = new AMap.Scale();
        map.addControl(scale); // 显示当前地图中心的比例尺


        const center = new AMap.LngLat(106.791541, 29.712158);
        const radius = 100;
        const circle = new AMap.Circle({
          center: center, //圆心
          radius: radius, //半径
          borderWeight: 3, //描边的宽度
          strokeColor: "#005579", //轮廓线颜色
          strokeOpacity: 1, //轮廓线透明度
          strokeWeight: 6, //轮廓线宽度
          fillOpacity: 0.4, //圆形填充透明度
          strokeStyle: "dashed", //轮廓线样式
          strokeDasharray: [10, 10],
          fillColor: "#1791fc", //圆形填充颜色
          zIndex: 50, //圆形的叠加顺序
        });
        map.add(circle);
        map.setFitView([circle]);

        const list = ref(
          [
            {
              lnt: 106.791554,
              lat: 29.712158,
              content: '外勤人员1',
            },
            {
              lnt: 106.79141,
              lat: 29.712158,
              content: '外勤人员2',
            },
            {
              lnt: 106.791241,
              lat: 29.712158,
              content: '外勤人员3',
            },
            {
              lnt: 106.791451,
              lat: 29.712158,
              content: '外勤人员4',
            },
            {
              lnt: 106.795541,
              lat: 29.712158,
              content: '外勤人员5',
            },
          ]
        )
        // 点标记显示内容，HTML要素字符串(官方例子)
        /*   const markerContent = ref('' +
              '<div class="custom-content-marker">' +
              '   <img src="//a.amap.com/jsapi_demos/static/demo-center/icons/dir-via-marker.png">' +
              '</div>'); */

        list.value.map((data) => {
          const marker = new AMap.Marker({
            position: new AMap.LngLat(data.lnt, data.lat),
            title: data.content, // 鼠标滑过点标记时的文字提示
            // content: markerContent.value, // 引入上面HTML要素字符串
            offset: new AMap.Pixel(-15, -15)
          })
          // 窗口信息
          const infoWindow = new AMap.InfoWindow({
            isCustom: true, // 自定义窗口,窗口外框以及内容完全按照content所设的值添加
            closeWhenClickMap: true, // 是否在鼠标点击地图关闭窗口
            content:
             `<div style="background-color: lightblue;width: 100px;height: 100px;border-radius: 6px;line-height: 80px;text-align: center;font-size:12px;">
              ${data.content}
              <button type="primary" @click="remindVisible = true"> 提醒</button>
              </div>`,
            offset: new AMap.Pixel(0, -15)
          })
          // 给marker添加click事件
          marker.on("click", (e) => {
            infoWindow.open(
              map,
              // 窗口信息的位置
              marker.getPosition(data.lnt, data.lat)
            );
          })
          // 给map添加zoomend事件,当缩放级别时触发
          map.on("zoomend", (e) => {
            // 关闭信息窗体
            map.clearInfoWindow(infoWindow);
          })
          marker.setMap(map);
        })

      })
      .catch((e) => {
        console.log(e);
      });

  });

  onUnmounted(() => {
    map?.destroy();
  });
</script>

<style scoped>
  #container {
    margin-top: 20px;
    width: 100%;
    height: 500px;
    border: solid 1px gray;
  }
</style>