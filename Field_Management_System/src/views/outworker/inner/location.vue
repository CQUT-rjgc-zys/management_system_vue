<template>
  <div>
    <button @click="getLocation">获取位置</button>
    <div v-if="error" class="error">{{ error }}</div>
    <div v-if="position">
      <p>经度: {{ position.coords.longitude }}</p>
      <p>纬度: {{ position.coords.latitude }}</p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';

    const position = ref(null);
    const error = ref(null);
    let longitude = null;
    let latitude = null;
    // getLocation 是异步函数等待geoPositon异步请求的完成
    const getLocation = () => {
      if ('geolocation' in navigator) {
        /* 地理位置服务可用 */
        var options = {
          enableHighAccuracy: true, //布尔值，表示系统是否使用最高精度来表示结果，注意，这会导致较慢的响应时间或者增加电量消耗（比如对于支持gps的移动设备来说）。如果值为false ，设备会通过更快响应以及/或者使用更少的电量等方法来尽可能的节约资源。默认值fasle
          timeout: 5000, //它表明的是设备必须在多长时间（单位毫秒）内返回一个位置。默认直到获取到位置才会返回值。
          maximumAge: 0 //表明可以返回多长时间（即最长年龄，单位毫秒）内的可获取的缓存位置。如果设置为 0, 说明设备不能使用一个缓存位置，而且必须去获取一个真实的当前位置。默认0
        }
        function success(position) {
          //position.coords (只读) 返回一个定义了当前位置的Coordinates对象。
          //position.timestamp (只读) 返回一个时间戳DOMTimeStamp， 这个时间戳表示获取到的位置的时间。
          var lat = position.coords.latitude //当前位置的纬度
          var lng = position.coords.longitude //当前位置精度
        }
        function error(err) {
          var errorType = ['您拒绝共享位置信息', '获取不到位置信息', '获取位置信息超时']
          console.log(errorType[err.code - 1])
        }
        navigator.geolocation.getCurrentPosition(success, error, options);
      } else {
        error.value = "Geolocation is not supported by this browser.";
      }
      /* 地理位置服务不可用 */
      console.log('无法获取您的位置，请检查定位是否开启或刷新重试')
    };


   /*  getLocation();
    longitude = getLocation().position.value.coords.longitude;
    latitude = getLocation().position.value.coords.latitude;
 */

console.log(longitude, latitude);
</script>

<style scoped>
.error {
  color: red;
}
</style>