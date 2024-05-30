<!-- 打卡 -->
<template>
  <div>
    <el-page-header @back="goBack">
      <template #content>
        <span class="text-large font-600 mr-3"> 打卡界面 </span>
      </template>
    </el-page-header>
    <div id='reportmap'>

    </div>

    <div>
      <div class="info">
        <h4 id="status"></h4>
        <p id="result"></p>
      </div>

      <div style="padding-left: 20px;">
        <el-row :gutter="20">
          <el-col :span="6">
            <div>
              <input readonly type="text" id="address" ref="addressRef" value="重庆市渝北区重庆理工大学两江校区">
            </div>
          </el-col>
          <el-col :span="12" :offset="1">
            <div>
              <div class="report" id="allowedReport" v-show="model ==='allowed'">
                <el-button type="primary" circle class="'report=btn'">打卡</el-button>
                <span>已到达目的范围,允许打卡</span>
              </div>
              <div class="report" id="notAllowedReport" v-show="model ==='notAllowed'">
                <el-button type="primary" circle class="'report=btn'" disabled>打卡</el-button>
                <span>未到达目的范围,不允许打卡</span>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
      <div>
      </div>
    </div>

  </div>

</template>

<script setup lang='ts'>
  import { ref, reactive, onMounted, onUnmounted, watchEffect } from 'vue';
  import { Check, Loading, Position } from '@element-plus/icons-vue'
  import AMapLoader from "@amap/amap-jsapi-loader";
  import { formContextKey } from 'element-plus';

  const model = ref('allowed');

  // 定义一个响应式数据来储存值
  // const addressRef = ref('');
  // const address = addressRef['_value._value'];
  // console.log(addressRef);
  // console.log(address);

  const addressValue = "重庆市渝北区重庆理工大学两江校区";

  // 返回外勤任务列表
  const router = useRouter();
  const goBack = () => {
    router.push("myTaskList");
  }
  /* ----------------------------------- */

  /* ----------------------------------- */
  // 加载地图
  let map = null;

  onMounted(() => {
    window._AMapSecurityConfig = {
      securityJsCode: "3bb27e8a128223c35f43cf5e04a8fedf",
    };

    // 加载js API
    AMapLoader.load({
      key: "47e625684e574f55150900888756c8c0", // 申请好的Web端开发者Key，首次调用 load 时必填
      version: "2.0",
      plugins: ["AMap.Geolocation", "AMap.Geocoder", "AMap.ToolBar", "AMap.Scale"]
    })
      .then(AMap => {
        map = new AMap.Map("reportmap", {
          // 设置地图容器id
          viewMode: "3D",
          zoom: 10,
          center: [106.397428, 29.90923],
          resizeEnable: true,
          terrain: true, //开启地形图
        });

        const scale = new AMap.Scale();
        map.addControl(scale);

        const geolocation = new AMap.Geolocation({
          enableHighAccuracy: true, //  是否使用高精度定位
          timeout: 10000, //  超过5秒后停止定位，默认：无穷大
          maximumAge: 0, // 定位结果缓存0毫秒，默认：0
          zoomToAccuracy: true,  //  定位成功后调整地图视野范围使定位位置及精度范围视野内可见，默认：false
          showCircle: false, //  定位成功后用圆圈表示定位精度范围，默认：true
        }); // 定位当前位置
        map.addControl(geolocation);
        geolocation.getCurrentPosition(function (status, result) {
          if (status === 'complete') {
            console.log(result);
            addMarker(result);
          } else {
            console.log("定位失败");
          }
        });
        // 外勤人员个人定位Marker实例
        const addMarker = (positionResult) => {
          // console.log(positionResult);
          let longitude = positionResult.position.KL;
          let latitude = positionResult.position.kT;
          let marker = new AMap.Marker({
            position: new AMap.LngLat(longitude, latitude),
            title: '外勤人员1',
          });
          marker.addTo(map);
        };


        /* 任务地点转为经纬度 */
        const geocoder = new AMap.Geocoder({});
        geocoder.getLocation(addressValue, function (status, result) {
          if (status === 'complete') {
            // console.log(result);
            var lnglat = result.geocodes[0].location
            // console.log(lnglat);
            taskCircle(lnglat);
          } else {
            log.error('根据地址查询位置失败');
          }
        });

        // 任务地点marker
        const taskCircle = (positionResult) => {
          console.log(positionResult);
          let longitude = positionResult.KL;
          let latitude = positionResult.kT;
          const center = new AMap.LngLat(longitude, latitude);
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


          // 创建一个 icon
          const endIcon = new AMap.Icon({
            size: new AMap.Size(25, 34),
            image: '//a.amap.com/jsapi_demos/static/demo-center/icons/dir-marker.png',
            imageSize: new AMap.Size(135, 40),
            imageOffset: new AMap.Pixel(-95, -3)
          });
          let c = new AMap.Marker({
            position: center,
            title: '外勤地点',
            icon: endIcon,
            offset: new AMap.Pixel(-13, -30)
          });
          map.add(c);
        };

      })
      .catch((e) => {
        console.log(e);
      });
  });

  onUnmounted(() => {
    map?.destroy();
  });



</script>

<style lang='scss' scoped>
  #reportmap {
    padding: 0;
    margin: 5px;
    width: 100%;
    height: 400px;
  }

  #address {
    border: none;
    width: 400px;
  }

  input {
    outline: none;
  }

  .report {
    display: flex;
    flex-direction: column;
    justify-content: space-evenly;
    align-items: center;
    padding-top: 10px;
    height: 200px;

    .is-circle {
      font-size: 20px;
      width: 100px;
      height: 100px;
    }
  }
</style>