<script setup>
import { RouterLink, RouterView } from 'vue-router';
import * as echarts from "echarts";
import { provide } from "vue";//使用provide/inject把echarts引入进来，在根组件里引入echarts,一般是App.vue
provide("echarts", echarts);

</script>

<template>
  <div id="app" >
      <router-view />
  </div>
</template>


<style >

html,
body,
#app {
  width: 100%;
  height: 100%;
}

#app {
  display: flex;
  padding-left: 0;
  text-align: center;
  margin-top: 0px;

}

body{
  margin:0;
  padding:0;
  border:0
}



</style>