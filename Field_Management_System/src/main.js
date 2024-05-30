// import './assets/main.css'

import { createApp } from 'vue';
import App from './App.vue';
import router from '@/router';

// 引入Echarts
// import { ECharts } from "vue-echarts";
// import 'echarts';  //全局引入

import * as echarts from 'echarts'

// 在main.js  中引入 Element
import * as ElementPlusIconsVue from '@element-plus/icons-vue';
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';

// 禁用浏览器自带滚动条
import TpScroll from '@/utils/tp-scroll.js';


const app = createApp(App);

// 挂载ECharts
// app.component('Echarts',ECharts);

app.config.globalProperties.$echarts = echarts

app.use(ElementPlus);

app.use(router);

app.mount('#app');
